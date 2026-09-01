package com.example.expense.feature.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.UiState
import com.example.expense.data.local.ExpenseEntity
import com.example.expense.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private const val FALLBACK_COLOR = "#7B61FF"

/**
 * "Ask FixMoney" - answers spending questions with a free, instant, fully
 * offline intent matcher first (see ChatIntentMatcher/OfflineChatAnswerBuilder,
 * both grounded in local Room data); only questions it doesn't recognize fall
 * through to the Firebase AI Logic / gemini-3.7-flash call (GeminiChatAssistant),
 * which costs a real API call and is subject to the free-tier rate limit.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage.Assistant(
                text = "Hi! Ask me anything about your spending — try a suggestion below or type your own question."
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    val suggestedQuestions = listOf(
        "Am I over budget?",
        "Biggest expense this week?",
        "Compare to last month"
    )

    fun sendMessage(question: String) {
        val trimmed = question.trim()
        if (trimmed.isEmpty() || _isSending.value) return

        val historyText = buildHistoryText()

        _messages.value = _messages.value +
            ChatMessage.User(trimmed) +
            ChatMessage.Assistant(text = "", isLoading = true)
        _isSending.value = true

        viewModelScope.launch {
            val context = buildDataContext()
            val intent = ChatIntentMatcher.match(trimmed, context.categoryColors.keys)

            val reply = if (intent != null) {
                Log.d("ai", "offline intent: $intent")
                OfflineChatAnswerBuilder.build(intent, context, repository)
            } else {
                val geminiResult = GeminiChatAssistant.ask(trimmed, context.summaryForGemini, historyText)
                Log.d("ai", "gemini: $geminiResult")

                // Gemini is primary; Groq is a second free-tier provider so one AI's
                // rate limit doesn't dead-end the chat (see GroqChatAssistant - it's a
                // no-op Failure if no API key is configured yet).
                val result = if (geminiResult is ChatAskResult.Success) {
                    geminiResult
                } else {
                    val groqResult = GroqChatAssistant.ask(trimmed, context.summaryForGemini, historyText)
                    Log.d("ai", "groq: $groqResult")
                    when {
                        groqResult is ChatAskResult.Success -> groqResult
                        // Only call it a quota problem when BOTH providers agree - any other
                        // combination (e.g. one of them mis-configured) should show the
                        // honest generic failure, not a misleading "wait a minute" message.
                        geminiResult == ChatAskResult.QuotaExceeded &&
                            groqResult == ChatAskResult.QuotaExceeded -> ChatAskResult.QuotaExceeded
                        else -> ChatAskResult.Failure
                    }
                }

                when (result) {
                    is ChatAskResult.Success -> {
                        val bars = result.answer.categories.map { c ->
                            val color = context.categoryColors[c.name.lowercase()] ?: FALLBACK_COLOR
                            CategoryBar(c.name, c.amount, color)
                        }
                        ChatMessage.Assistant(text = result.answer.text, categories = bars)
                    }
                    ChatAskResult.QuotaExceeded -> ChatMessage.Assistant(
                        text = "You've hit the free AI usage limit for now — wait about a minute and try again.",
                        isError = true
                    )
                    ChatAskResult.Failure -> ChatMessage.Assistant(
                        text = "Sorry, I couldn't reach the assistant just now — try again in a moment.",
                        isError = true
                    )
                }
            }
            _messages.value = _messages.value.dropLast(1) + reply
            _isSending.value = false
        }
    }

    /** Prior turns as plain text, loading/error placeholders excluded. */
    private fun buildHistoryText(): String =
        _messages.value
            .filterNot { it is ChatMessage.Assistant && (it.isLoading || it.isError) }
            .joinToString("\n") { msg ->
                when (msg) {
                    is ChatMessage.User -> "Q: ${msg.text}"
                    is ChatMessage.Assistant -> "A: ${msg.text}"
                }
            }
            .takeLast(2000)

    private suspend fun buildDataContext(): ChatDataContext {
        val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
        val previousMonth = previousMonthKey(currentMonth)

        val currentExpenses = repository.getExpensesForExportMonth(currentMonth)
        val previousExpenses = repository.getExpensesForExportMonth(previousMonth)
        val budgetResult = repository.getBudgets(currentMonth)
        val totalBudget = (budgetResult as? UiState.Success)?.data?.data?.sumOf { it.limitAmount } ?: 0

        val colorMap = (currentExpenses + previousExpenses)
            .associateBy(
                keySelector = { it.categoryName.lowercase() },
                valueTransform = { it.categoryColor }
            )

        return ChatDataContext(
            currentMonthKey = currentMonth,
            previousMonthKey = previousMonth,
            currentExpenses = currentExpenses,
            previousExpenses = previousExpenses,
            totalBudget = totalBudget,
            categoryColors = colorMap
        )
    }

    private fun previousMonthKey(monthKey: String): String {
        val (year, month) = monthKey.split("-").map { it.toInt() }
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1)
        cal.add(Calendar.MONTH, -1)
        return SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(cal.time)
    }
}

/** Room data gathered once per question - shared by the offline answer builder
 * (OfflineChatAnswerBuilder) and, via [summaryForGemini], the Gemini fallback prompt. */
data class ChatDataContext(
    val currentMonthKey: String,
    val previousMonthKey: String,
    val currentExpenses: List<ExpenseEntity>,
    val previousExpenses: List<ExpenseEntity>,
    val totalBudget: Int,
    val categoryColors: Map<String, String>
) {
    val currentTotal: Int get() = currentExpenses.sumAmount()
    val previousTotal: Int get() = previousExpenses.sumAmount()

    fun currentByCategory(): List<Pair<String, Int>> = currentExpenses.byCategoryList()
    fun previousByCategory(): List<Pair<String, Int>> = previousExpenses.byCategoryList()

    val summaryForGemini: String
        get() = buildString {
            appendLine("Current month (${monthDisplay(currentMonthKey)}):")
            appendLine("- Total spent: $currentTotal")
            appendLine("- Total budget: $totalBudget")
            appendLine("- By category: ${formatCategoryList(currentByCategory())}")
            appendLine()
            appendLine("Previous month (${monthDisplay(previousMonthKey)}):")
            appendLine("- Total spent: $previousTotal")
            appendLine("- By category: ${formatCategoryList(previousByCategory())}")
        }
}

internal fun List<ExpenseEntity>.sumAmount(): Int =
    sumOf { it.amount.toDoubleOrNull()?.toInt() ?: 0 }

internal fun List<ExpenseEntity>.byCategoryList(): List<Pair<String, Int>> =
    groupBy { it.categoryName }
        .mapValues { (_, list) -> list.sumOf { it.amount.toDoubleOrNull()?.toInt() ?: 0 } }
        .entries.sortedByDescending { it.value }
        .map { it.key to it.value }

private fun formatCategoryList(list: List<Pair<String, Int>>): String =
    list.joinToString(", ") { "${it.first} ${it.second}" }.ifBlank { "no expenses recorded" }

private fun monthDisplay(monthKey: String): String {
    val (year, month) = monthKey.split("-").map { it.toInt() }
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1)
    return SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)
}
