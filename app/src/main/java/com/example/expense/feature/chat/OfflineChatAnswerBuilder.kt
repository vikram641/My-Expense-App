package com.example.expense.feature.chat

import com.example.expense.data.local.ExpenseEntity
import com.example.expense.data.repository.Repository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val FALLBACK_COLOR = "#7B61FF"

/** Builds an answer directly from local Room data for a recognized [ChatIntent] -
 * no network call, no AI, just templated text and the real numbers. */
object OfflineChatAnswerBuilder {

    suspend fun build(intent: ChatIntent, context: ChatDataContext, repository: Repository): ChatMessage.Assistant =
        when (intent) {
            ChatIntent.BudgetStatus -> budgetStatus(context)
            is ChatIntent.BiggestExpense -> biggestExpense(intent.scope, context, repository)
            ChatIntent.CompareMonths -> compareMonths(context)
            is ChatIntent.CategorySpend -> categorySpend(intent.categoryName, intent.scope, context, repository)
            is ChatIntent.TotalSpend -> totalSpend(intent.scope, context, repository)
        }

    private fun budgetStatus(context: ChatDataContext): ChatMessage.Assistant {
        if (context.totalBudget <= 0) {
            return ChatMessage.Assistant(text = "You haven't set a budget for this month yet.")
        }
        val diff = context.currentTotal - context.totalBudget
        val text = if (diff > 0) {
            "You're over budget by ₹$diff this month — you've spent ₹${context.currentTotal} of your ₹${context.totalBudget} budget."
        } else {
            "You're within budget — ₹${-diff} left of your ₹${context.totalBudget} budget this month."
        }
        return ChatMessage.Assistant(text = text)
    }

    private suspend fun biggestExpense(
        scope: TimeScope, context: ChatDataContext, repository: Repository
    ): ChatMessage.Assistant {
        val (label, expenses) = expensesForScope(scope, context, repository)
        val byCategory = expenses.byCategoryList()
        if (byCategory.isEmpty()) {
            return ChatMessage.Assistant(text = "No expenses recorded $label.")
        }
        val (topName, topAmount) = byCategory.first()
        return ChatMessage.Assistant(
            text = "Your biggest expense $label was $topName at ₹$topAmount.",
            categories = toBars(byCategory.take(5), context)
        )
    }

    private fun compareMonths(context: ChatDataContext): ChatMessage.Assistant {
        val diff = context.currentTotal - context.previousTotal
        val text = when {
            context.previousTotal == 0 ->
                "You've spent ₹${context.currentTotal} this month; there's no data from last month to compare against."
            diff > 0 ->
                "You've spent ₹${context.currentTotal} this month vs ₹${context.previousTotal} last month — that's ₹$diff more."
            diff < 0 ->
                "You've spent ₹${context.currentTotal} this month vs ₹${context.previousTotal} last month — that's ₹${-diff} less."
            else ->
                "You've spent the same amount this month as last month — ₹${context.currentTotal}."
        }
        return ChatMessage.Assistant(text = text, categories = toBars(context.currentByCategory().take(5), context))
    }

    private suspend fun categorySpend(
        categoryName: String, scope: TimeScope, context: ChatDataContext, repository: Repository
    ): ChatMessage.Assistant {
        val (label, expenses) = expensesForScope(scope, context, repository)
        val matching = expenses.filter { it.categoryName.lowercase() == categoryName }
        val amount = matching.sumAmount()
        val displayName = matching.firstOrNull()?.categoryName ?: categoryName
        val text = if (amount == 0) {
            "You haven't spent anything on $displayName $label."
        } else {
            "You spent ₹$amount on $displayName $label."
        }
        return ChatMessage.Assistant(text = text)
    }

    private suspend fun totalSpend(
        scope: TimeScope, context: ChatDataContext, repository: Repository
    ): ChatMessage.Assistant {
        val (label, expenses) = expensesForScope(scope, context, repository)
        return ChatMessage.Assistant(
            text = "You've spent ₹${expenses.sumAmount()} $label.",
            categories = toBars(expenses.byCategoryList().take(5), context)
        )
    }

    private suspend fun expensesForScope(
        scope: TimeScope, context: ChatDataContext, repository: Repository
    ): Pair<String, List<ExpenseEntity>> = when (scope) {
        TimeScope.CURRENT_MONTH -> "this month" to context.currentExpenses
        TimeScope.PREVIOUS_MONTH -> "last month" to context.previousExpenses
        TimeScope.THIS_WEEK -> "this week" to fetchLastNDays(repository, 7)
    }

    private suspend fun fetchLastNDays(repository: Repository, days: Int): List<ExpenseEntity> {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val toDate = fmt.format(Calendar.getInstance().time)
        val fromCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -(days - 1)) }
        val fromDate = fmt.format(fromCal.time)
        return repository.getExpensesForExportRange(fromDate, toDate)
    }

    private fun toBars(entries: List<Pair<String, Int>>, context: ChatDataContext): List<CategoryBar> =
        entries.map { (name, amount) ->
            CategoryBar(name, amount, context.categoryColors[name.lowercase()] ?: FALLBACK_COLOR)
        }
}
