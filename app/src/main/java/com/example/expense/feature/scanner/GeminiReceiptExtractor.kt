package com.example.expense.feature.scanner

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "GeminiReceiptExtractor"

enum class ReceiptImageStatus { OK, BLURRY, NOT_A_RECEIPT }

data class ExtractedReceipt(
    val amount: String? = null,
    val date: String? = null,
    val note: String? = null,
    val category: String? = null,
    val status: ReceiptImageStatus = ReceiptImageStatus.OK
)

/**
 * Sends the captured receipt photo directly to Gemini (via Firebase AI Logic,
 * Gemini Developer API backend, free tier) and asks for every field in one
 * multimodal call, instead of local OCR + regex heuristics. The photo is only
 * held in memory for this call - never written to disk.
 *
 * Returns null only on a hard failure (network/timeout/exception) - the caller
 * should show a generic retry message in that case. A soft failure (blurry
 * photo, or a photo that isn't a receipt at all) still returns a result, with
 * [ExtractedReceipt.status] set so the caller can show a specific message.
 */
object GeminiReceiptExtractor {

    suspend fun extract(bitmap: Bitmap, categoryNames: List<String>): ExtractedReceipt? {
        return try {
            val schema = Schema.obj(
                mapOf(
                    "status" to Schema.enumeration(listOf("ok", "blurry", "not_receipt")),
                    "amount" to Schema.integer(),
                    "date" to Schema.string(),
                    "merchant" to Schema.string(),
                    "item" to Schema.string(),
                    "category" to Schema.enumeration(categoryNames + "NONE")
                ),
                listOf("amount", "date", "merchant", "item", "category")
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
                image(bitmap)
                text(
                    "You are looking at a photo taken by someone trying to scan a store receipt " +
                        "into an expense-tracking app. First decide the image quality/content:\n" +
                        "- status = \"blurry\" if it IS a receipt but too blurry, dark, or out of " +
                        "focus to read reliably.\n" +
                        "- status = \"not_receipt\" if the image clearly isn't a store receipt or " +
                        "bill at all (e.g. a random photo, a screen showing something unrelated, " +
                        "a blank/empty frame).\n" +
                        "- status = \"ok\" if it's a readable receipt/bill.\n\n" +
                        "Only when status is \"ok\", also extract:\n" +
                        "- amount: the FINAL amount actually paid, as a whole number (no currency " +
                        "symbol, no decimals). If the receipt lists multiple money lines (subtotal, " +
                        "tax/GST/VAT, service charge, discount, tip, grand total), use the grand " +
                        "total / amount paid / net total - never a subtotal or a tax/discount line " +
                        "by itself. Round to the nearest whole number.\n" +
                        "- date: the purchase date, output as yyyy-MM-dd. Receipts write dates in " +
                        "different regional orders (day/month/year or month/day/year); use context " +
                        "on the receipt (e.g. a day value above 12 fixes the order) to resolve it, " +
                        "and if genuinely ambiguous prefer day/month/year. Never output an " +
                        "impossible date (e.g. month 13).\n" +
                        "- merchant: the store/business name.\n" +
                        "- item: a short name for the main item(s) purchased (e.g. \"Regular Pan " +
                        "Pizza\" or \"Groceries\" - keep it brief).\n" +
                        "- category: the single best matching category from the allowed list, or " +
                        "NONE if nothing fits confidently.\n" +
                        "Leave a field out if it isn't clearly present on the receipt - never guess " +
                        "a value that isn't actually printed."
                )
            }

            val text = withTimeout(15_000) { model.generateContent(prompt).text } ?: return null
            val json = JSONObject(text)

            val status = when (json.optString("status").lowercase()) {
                "blurry" -> ReceiptImageStatus.BLURRY
                "not_receipt" -> ReceiptImageStatus.NOT_A_RECEIPT
                else -> ReceiptImageStatus.OK
            }

            if (status != ReceiptImageStatus.OK) {
                return ExtractedReceipt(status = status)
            }

            val category = json.optString("category").takeIf {
                it.isNotBlank() && !it.equals("NONE", ignoreCase = true)
            }
            val merchant = json.optString("merchant").takeIf { it.isNotBlank() }
            val item = json.optString("item").takeIf { it.isNotBlank() }
            val note = when {
                item != null && merchant != null -> "$item @ $merchant"
                item != null -> item
                else -> merchant
            }

            Log.d(TAG, "extracted: category=$category merchant=$merchant item=$item")

            ExtractedReceipt(
                amount = if (json.has("amount") && !json.isNull("amount")) {
                    json.optInt("amount").takeIf { it > 0 }?.toString()
                } else null,
                date = json.optString("date").takeIf { it.isNotBlank() },
                note = note,
                category = category,
                status = ReceiptImageStatus.OK
            )
        } catch (e: TimeoutCancellationException) {
            Log.w(TAG, "extraction timed out", e)
            null
        } catch (e: Exception) {
            Log.w(TAG, "extraction failed", e)
            null
        }
    }
}
