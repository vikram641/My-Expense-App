package com.example.expense.feature.voicequickadd

import com.example.expense.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val GROQ_URL = "https://api.groq.com/openai/v1/chat/completions"
// Same model GroqChatAssistant already uses/verified for this project's key - see its
// own comment for why (llama-3.3-70b-versatile 404s on this key's catalog).
private const val GROQ_MODEL = "openai/gpt-oss-20b"

/**
 * Second-tier fallback for voice quick-add, mirroring the Gemini-then-Groq pattern
 * ChatViewModel already uses for the chat assistant: same job as
 * [GeminiVoiceExpenseParser], different free-tier provider, so one provider's rate
 * limit doesn't block adding an expense. Throws (never returns null) on failure,
 * same contract as GeminiVoiceExpenseParser, including when no Groq key is configured.
 */
object GroqVoiceExpenseParser {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun parse(spokenText: String, categoryNames: List<String>): ParsedVoiceExpense {
        val apiKey = BuildConfig.GROQ_API_KEY
        if (apiKey.isBlank()) throw IllegalStateException("No Groq API key configured")

        return withContext(Dispatchers.IO) {
            withTimeout(20_000) { call(apiKey, spokenText, categoryNames) }
        }
    }

    private fun call(apiKey: String, spokenText: String, categoryNames: List<String>): ParsedVoiceExpense {
        val systemPrompt =
            "You are parsing one short sentence someone just spoke to quickly log a single " +
                "expense into an expense-tracking app, for example: \"150 on coffee\", " +
                "\"₹500 for groceries\", \"spent 40 rupees on auto\", \"add 1200 electricity bill\".\n\n" +
                "Extract:\n" +
                "- amount: the number of rupees mentioned, as a whole number with no currency " +
                "symbol or decimals. If no number that clearly represents an amount of money is " +
                "present, use 0.\n" +
                "- category: the single best matching category from this allowed list: " +
                "${categoryNames.joinToString(", ")} - or \"NONE\" if nothing fits confidently.\n" +
                "- note: a short 2-4 word description of what the expense was for, close to the " +
                "user's own words (e.g. \"Coffee\", \"Auto fare\", \"Electricity bill\").\n\n" +
                "Respond with ONLY a JSON object shaped exactly like " +
                "{\"amount\": integer, \"category\": string, \"note\": string} - no other text, " +
                "no markdown code fences."

        val payload = JSONObject().apply {
            put("model", GROQ_MODEL)
            put("temperature", 0.2)
            put("response_format", JSONObject().put("type", "json_object"))
            put(
                "messages",
                JSONArray().apply {
                    put(JSONObject().put("role", "system").put("content", systemPrompt))
                    put(JSONObject().put("role", "user").put("content", spokenText))
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
            if (!response.isSuccessful || bodyText.isNullOrBlank()) {
                throw IllegalStateException("Groq call failed: ${response.code} $bodyText")
            }

            val content = JSONObject(bodyText)
                .optJSONArray("choices")
                ?.optJSONObject(0)
                ?.optJSONObject("message")
                ?.optString("content")
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalStateException("Groq returned no content")

            val json = JSONObject(content)
            val category = json.optString("category").takeIf {
                it.isNotBlank() && !it.equals("NONE", ignoreCase = true)
            }
            val note = json.optString("note").takeIf { it.isNotBlank() } ?: spokenText

            return ParsedVoiceExpense(
                amount = json.optInt("amount").coerceAtLeast(0),
                category = category,
                note = note
            )
        }
    }
}
