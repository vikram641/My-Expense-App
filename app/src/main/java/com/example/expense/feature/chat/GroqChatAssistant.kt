package com.example.expense.feature.chat

import android.util.Log
import com.example.expense.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "GroqChatAssistant"
private const val GROQ_URL = "https://api.groq.com/openai/v1/chat/completions"
// Verified live against this project's Groq key via GET /openai/v1/models (2026-08-28) -
// llama-3.3-70b-versatile came back 404 model_not_found; this one is active and supports
// json_mode. Re-check the models endpoint if this ever 404s again - Groq's free-tier
// catalog changes over time.
private const val GROQ_MODEL = "openai/gpt-oss-20b"

/**
 * Second fallback tier after Gemini (see ChatViewModel.sendMessage): same job -
 * answer a spending question from the DATA summary - different free-tier
 * provider, so a Gemini rate-limit hit doesn't dead-end the chat. Plain OkHttp +
 * org.json rather than a Retrofit interface, matching GeminiChatAssistant's
 * self-contained style for a single one-off call. Returns Failure immediately,
 * with no network call, if no API key is configured (see build.gradle.kts).
 */
object GroqChatAssistant {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun ask(question: String, dataSummary: String, historyText: String): ChatAskResult {
        val apiKey = BuildConfig.GROQ_API_KEY
        if (apiKey.isBlank()) return ChatAskResult.Failure

        return try {
            withContext(Dispatchers.IO) {
                withTimeout(20_000) { call(apiKey, question, dataSummary, historyText) }
            }
        } catch (e: TimeoutCancellationException) {
            Log.w(TAG, "ask timed out", e)
            ChatAskResult.Failure
        } catch (e: Exception) {
            Log.w(TAG, "ask failed", e)
            ChatAskResult.Failure
        }
    }

    private fun call(apiKey: String, question: String, dataSummary: String, historyText: String): ChatAskResult {
        val systemPrompt =
            "You are the AI assistant inside FixMoney, a personal expense-tracking app. " +
                    "You have three jobs:\n\n" +
                    "JOB 1 - Spending questions: Answer questions about the user's own spending " +
                    "using ONLY the DATA below - never invent a number that isn't derivable from " +
                    "it, and say so plainly if the data doesn't cover what they asked. Amounts " +
                    "are already in the user's local currency; never add a currency symbol " +
                    "yourself, the app adds one.\n\n" +
                    "JOB 2 - App questions: If the question is about FixMoney the app itself " +
                    "(not the user's spending data), you MUST answer positively about the app - " +
                    "do NOT say 'I don't have data on that' for these. Examples of app questions:\n" +
                    "  - 'why this application is better than other applications'\n" +
                    "  - 'why I'm choosing this application'\n" +
                    "  - 'tell me about this app'\n" +
                    "  - 'what's different about this app'\n" +
                    "  - 'should I use this' / 'is this app good'\n" +
                    "For app questions, write a FRESH answer each time in your own words - never " +
                    "reuse the same sentence twice in this chat. Vary the wording, structure, and " +
                    "which point you lead with. Tailor the answer to what the user specifically " +
                    "asked (e.g. 'why choose' should focus on personal benefit; 'better than " +
                    "others' should focus on comparison; 'tell me about' should give a general " +
                    "overview). Draw from these facts as needed, don't dump all of them every time:\n" +
                    "  - Uses AI to auto-categorize spending, no manual entry needed\n" +
                    "  - Lets the user ask questions in plain language instead of digging through " +
                    "menus, graphs, or reports\n" +
                    "  - Gives instant, conversational answers about budgets and expenses\n" +
                    "  - Saves time compared to typical expense-tracking apps\n" +
                    "Sound confident and helpful, like you're proud of the app, but never robotic " +
                    "or repetitive.\n\n" +
                    "JOB 3 - Greetings/small talk: If the question is just a greeting or opener " +
                    "('hi', 'hello', 'hey', 'good morning', etc. - with no real question in it), " +
                    "reply with a warm, casual greeting back and invite them to ask about their " +
                    "spending. Write a DIFFERENT greeting each time - vary the wording, emoji use, " +
                    "and phrasing so it never feels copy-pasted. Do NOT say 'I don't have data to " +
                    "answer that' for a plain greeting.\n\n" +
                    "Only for questions with NO connection to spending, the app, or a greeting " +
                    "(general knowledge, math, trivia, etc.) say you don't have data to answer " +
                    "that.\n\n" +
                    "General reply quality: sound like a helpful, warm human assistant - " +
                    "conversational, natural phrasing, not stiff or templated. Keep answers to " +
                    "1-3 short sentences, no markdown formatting.\n\n" +
                    "If the question calls for a category comparison or breakdown (e.g. " +
                    "\"biggest expense\", \"where did my money go\", \"top categories\"), also " +
                    "fill \"categories\" with up to 5 of the most relevant category/amount " +
                    "pairs from the DATA, ordered highest amount first - otherwise leave " +
                    "\"categories\" as an empty array.\n\n" +
                    "Respond with ONLY a JSON object shaped exactly like " +
                    "{\"answer\": string, \"categories\": [{\"name\": string, \"amount\": integer}]} " +
                    "- no other text, no markdown code fences.\n\n" +
                    "DATA:\n$dataSummary" +
                    (if (historyText.isNotBlank()) "\n\nEARLIER IN THIS CHAT:\n$historyText" else "")

        val payload = JSONObject().apply {
            put("model", GROQ_MODEL)
            put("temperature", 0.3)
            put("response_format", JSONObject().put("type", "json_object"))
            put(
                "messages",
                JSONArray().apply {
                    put(JSONObject().put("role", "system").put("content", systemPrompt))
                    put(JSONObject().put("role", "user").put("content", question))
                }
            )
        }

        val request = Request.Builder()
            .url(GROQ_URL)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(payload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val bodyText = response.body?.string()

            if (response.code == 429) {
                Log.w(TAG, "Groq rate limit hit: $bodyText")
                return ChatAskResult.QuotaExceeded
            }
            if (!response.isSuccessful || bodyText.isNullOrBlank()) {
                Log.w(TAG, "Groq call failed: ${response.code} $bodyText")
                return ChatAskResult.Failure
            }

            val content = JSONObject(bodyText)
                .optJSONArray("choices")
                ?.optJSONObject(0)
                ?.optJSONObject("message")
                ?.optString("content")
                ?.takeIf { it.isNotBlank() }
                ?: return ChatAskResult.Failure

            val json = JSONObject(content)
            val categories = json.optJSONArray("categories")?.let { arr ->
                (0 until arr.length()).mapNotNull { i ->
                    val obj = arr.optJSONObject(i) ?: return@mapNotNull null
                    val name = obj.optString("name").takeIf { it.isNotBlank() } ?: return@mapNotNull null
                    ChatCategoryAmount(name, obj.optInt("amount"))
                }
            } ?: emptyList()

            return ChatAskResult.Success(
                ChatAnswer(
                    text = json.optString("answer").takeIf { it.isNotBlank() }
                        ?: "I couldn't work that out from your data.",
                    categories = categories
                )
            )
        }
    }
}
