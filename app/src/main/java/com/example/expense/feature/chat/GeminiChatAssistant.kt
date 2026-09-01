package com.example.expense.feature.chat

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.QuotaExceededException
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "GeminiChatAssistant"

data class ChatCategoryAmount(val name: String, val amount: Int)

data class ChatAnswer(val text: String, val categories: List<ChatCategoryAmount> = emptyList())

/** Distinguishes "the free-tier rate limit was hit" from any other failure, so the UI
 * can tell the user something accurate instead of a generic "couldn't reach" message. */
sealed class ChatAskResult {
    data class Success(val answer: ChatAnswer) : ChatAskResult()
    object QuotaExceeded : ChatAskResult()
    object Failure : ChatAskResult()
}

/**
 * Text-only counterpart to GeminiReceiptExtractor: answers a spending question
 * grounded in a plain-text summary of the user's own local Room data (built by
 * ChatViewModel, never sent anywhere except this one Gemini call). One-shot
 * per question - conversational continuity comes from replaying prior Q&A as
 * plain text in [historyText] rather than the SDK's startChat()/multi-turn
 * Content API, to stay on the single-Content generateContent() shape already
 * verified working for the receipt scanner.
 */
object GeminiChatAssistant {

    suspend fun ask(question: String, dataSummary: String, historyText: String): ChatAskResult {
        return try {
            val schema = Schema.obj(
                mapOf(
                    "answer" to Schema.string(),
                    "categories" to Schema.array(
                        Schema.obj(
                            mapOf(
                                "name" to Schema.string(),
                                "amount" to Schema.integer()
                            ),
                            listOf("name", "amount")
                        )
                    )
                ),
                listOf("answer")
            )

            val model = Firebase.ai(backend = GenerativeBackend.googleAI())
                .generativeModel(
                    "gemini-3.7-flash",
                    generationConfig {
                        responseMimeType = "application/json"
                        responseSchema = schema
                    }
                )




            val prompt = content {
                text(
                    "You are the AI assistant inside FixMoney, a personal expense-tracking app. " +
                            "You have two jobs:\n\n" +
                            "JOB 1 - Spending questions: Answer questions about the user's own spending " +
                            "using ONLY the DATA below - never invent a number that isn't derivable from " +
                            "it, and say so plainly if the data doesn't cover what they asked. Amounts " +
                            "are already in the user's local currency; never add a currency symbol " +
                            "yourself, the app adds one.\n\n" +
                            "JOB 2 - App questions: If the question is about FixMoney the app itself " +
                            "(not the user's spending data), you MUST answer positively about the app - " +
                            "do NOT say 'I don't have data on that' for these. Examples of app questions " +
                            "and how to treat them:\n" +
                            "  - 'why this application is better than other applications' -> app question\n" +
                            "  - 'why I'm choosing this application' -> app question\n" +
                            "  - 'tell me about this app' -> app question\n" +
                            "  - 'what's different about this app' -> app question\n" +
                            "  - 'should I use this' / 'is this app good' -> app question\n" +
                            "For ALL app questions, answer using this angle: FixMoney is an AI-powered " +
                            "expense tracker that automatically categorizes spending and lets you just " +
                            "ask questions in plain language (like this chat) instead of digging through " +
                            "menus, graphs, or manual entry like other expense apps require. Sound " +
                            "confident and helpful, like you're proud of the app.\n\n" +
                            "Only for questions with NO connection to spending or the app (general " +
                            "knowledge, math, trivia, etc.) say you don't have data to answer that.\n\n" +
                            "Keep the answer to 1-3 short sentences, conversational, no markdown formatting.\n\n" +
                            "If the question calls for a category comparison or breakdown (e.g. " +
                            "\"biggest expense\", \"where did my money go\", \"top categories\"), also " +
                            "fill \"categories\" with up to 5 of the most relevant category/amount " +
                            "pairs from the DATA, ordered highest amount first - otherwise leave " +
                            "\"categories\" as an empty array.\n\n" +
                            "DATA:\n$dataSummary\n\n" +
                            (if (historyText.isNotBlank()) "EARLIER IN THIS CHAT:\n$historyText\n\n" else "") +
                            "QUESTION: $question"
                )
            }

            // 30s, not the scanner's 15s: this is a foreground chat send under whatever
            // connection the user has at the moment, not a capture flow with its own retry UI.
            val text = withTimeout(30_000) { model.generateContent(prompt).text }
                ?: return ChatAskResult.Failure
            val json = JSONObject(text)

            val categories = json.optJSONArray("categories")?.let { arr ->
                (0 until arr.length()).mapNotNull { i ->
                    val obj = arr.optJSONObject(i) ?: return@mapNotNull null
                    val name = obj.optString("name").takeIf { it.isNotBlank() } ?: return@mapNotNull null
                    ChatCategoryAmount(name, obj.optInt("amount"))
                }
            } ?: emptyList()

            ChatAskResult.Success(
                ChatAnswer(
                    text = json.optString("answer").takeIf { it.isNotBlank() }
                        ?: "I couldn't work that out from your data.",
                    categories = categories
                )
            )
        } catch (e: QuotaExceededException) {
            Log.w(TAG, "ask hit the free-tier rate limit", e)
            ChatAskResult.QuotaExceeded
        } catch (e: TimeoutCancellationException) {
            Log.w(TAG, "ask timed out", e)
            ChatAskResult.Failure
        } catch (e: Exception) {
            Log.w(TAG, "ask failed", e)
            ChatAskResult.Failure
        }
    }
}
