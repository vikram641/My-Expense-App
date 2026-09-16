package com.example.expense.feature.voicequickadd

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

data class ParsedVoiceExpense(
    val amount: Int,
    val category: String?,
    val note: String
)

/**
 * Turns one short spoken sentence ("150 on coffee", "spent 40 rupees on auto")
 * into a structured expense, the same "send it to Gemini with a forced JSON
 * schema" approach as [com.example.expense.feature.scanner.GeminiReceiptExtractor],
 * just text-only instead of multimodal.
 *
 * Throws on a hard failure (network/timeout/exception) - the caller decides how to
 * surface it. An amount of 0 is a soft failure (no number recognized in the speech)
 * and still returns a result, so the caller can ask the user to fill it in rather
 * than losing the note.
 */
object GeminiVoiceExpenseParser {

    suspend fun parse(spokenText: String, categoryNames: List<String>): ParsedVoiceExpense {
        val schema = Schema.obj(
            mapOf(
                "amount" to Schema.integer(),
                "category" to Schema.enumeration(categoryNames + "NONE"),
                "note" to Schema.string()
            ),
            listOf("amount", "category", "note")
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
                "You are parsing one short sentence someone just spoke to quickly log a " +
                    "single expense into an expense-tracking app, for example: " +
                    "\"150 on coffee\", \"₹500 for groceries\", \"spent 40 rupees on auto\", " +
                    "\"add 1200 electricity bill\".\n\n" +
                    "Extract:\n" +
                    "- amount: the number of rupees mentioned, as a whole number with no " +
                    "currency symbol or decimals. If no number that clearly represents an " +
                    "amount of money is present, use 0.\n" +
                    "- category: the single best matching category from the allowed list, or " +
                    "NONE if nothing fits confidently.\n" +
                    "- note: a short 2-4 word description of what the expense was for, close " +
                    "to the user's own words (e.g. \"Coffee\", \"Auto fare\", \"Electricity bill\").\n\n" +
                    "Recognized speech: \"$spokenText\""
            )
        }

        val text = withTimeout(15_000) { model.generateContent(prompt).text }
            ?: throw IllegalStateException("Gemini returned an empty response")
        val json = JSONObject(text)

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
