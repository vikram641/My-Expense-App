package com.example.expense.core.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.expense.core.UiState
import com.example.expense.data.local.ExpenseEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Settings > Export Data. Writes expenses as CSV (not a real .xlsx - avoids pulling in a
 * heavy library like Apache POI for what a spreadsheet app already opens natively) into the
 * shared Downloads/Expense Exports/ folder.
 *
 * On API 29+ this goes through MediaStore (scoped storage, no permission needed). On API
 * 24-28 MediaStore's Downloads collection doesn't exist yet, so it falls back to a direct
 * File write under the legacy public Downloads dir, which needs WRITE_EXTERNAL_STORAGE
 * (declared maxSdkVersion="28" in the manifest, and must already be granted by the caller -
 * this class doesn't request permissions itself).
 */
class ExpenseCsvExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun export(expenses: List<ExpenseEntity>, fileName: String): UiState<String> =
        withContext(Dispatchers.IO) {
            if (expenses.isEmpty()) {
                return@withContext UiState.Error("No expenses found for that period")
            }
            try {
                val csv = buildCsv(expenses)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    writeViaMediaStore(csv, fileName)
                } else {
                    writeViaLegacyFile(csv, fileName)
                }
                UiState.Success("Saved ${expenses.size} expenses to Downloads/Expense Exports/$fileName")
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Export failed")
            }
        }

    private fun buildCsv(expenses: List<ExpenseEntity>): String = buildString {
        append("Date,Category,Amount,Currency,Note\n")
        expenses.forEach { expense ->
            append(csvField(expense.date)).append(',')
            append(csvField(expense.categoryName)).append(',')
            append(csvField(expense.amount)).append(',')
            append(csvField(expense.currency)).append(',')
            append(csvField(expense.note)).append('\n')
        }
    }

    /** Quotes a field if it contains a comma, quote, or newline; doubles up internal quotes -
     * standard CSV escaping, since notes are free text and could contain either. */
    private fun csvField(value: String): String =
        if (value.any { it == ',' || it == '"' || it == '\n' }) {
            "\"${value.replace("\"", "\"\"")}\""
        } else value

    private fun writeViaMediaStore(csv: String, fileName: String) {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/Expense Exports")
        }
        val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: throw IllegalStateException("Could not create export file")
        context.contentResolver.openOutputStream(uri)?.use { it.write(csv.toByteArray()) }
            ?: throw IllegalStateException("Could not open export file for writing")
    }

    private fun writeViaLegacyFile(csv: String, fileName: String) {
        @Suppress("DEPRECATION")
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Expense Exports")
        if (!dir.exists()) dir.mkdirs()
        FileOutputStream(File(dir, fileName)).use { it.write(csv.toByteArray()) }
    }
}
