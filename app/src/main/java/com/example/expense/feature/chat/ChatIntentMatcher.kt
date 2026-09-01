package com.example.expense.feature.chat

enum class TimeScope { CURRENT_MONTH, PREVIOUS_MONTH, THIS_WEEK }

sealed class ChatIntent {
    object BudgetStatus : ChatIntent()
    data class BiggestExpense(val scope: TimeScope) : ChatIntent()
    object CompareMonths : ChatIntent()
    data class CategorySpend(val categoryName: String, val scope: TimeScope) : ChatIntent()
    data class TotalSpend(val scope: TimeScope) : ChatIntent()
}

/**
 * Free, fully offline keyword matcher for the fixed set of question shapes this
 * chat actually needs to answer instantly (the suggested chips + obvious phrasing
 * variants). Returns null for anything else, so ChatViewModel falls back to the
 * real Gemini call only for genuinely open-ended questions - keeping the common
 * path free of API calls (and the free-tier rate limit) entirely.
 */
object ChatIntentMatcher {

    fun match(question: String, knownCategories: Set<String>): ChatIntent? {
        val q = question.trim().lowercase()
        if (q.isEmpty()) return null

        val scope = detectScope(q)

        if (containsAny(
                q, "over budget", "under budget", "budget left", "budget remaining",
                "remaining budget", "am i over", "how much budget", "budget status",
                "left in my budget", "left in budget"
            )
        ) {
            return ChatIntent.BudgetStatus
        }

        if (containsAny(q, "compare", "vs last month", "versus last month", "compared to")) {
            return ChatIntent.CompareMonths
        }

        if (containsAny(
                q, "biggest expense", "biggest spend", "most expensive", "highest spend",
                "top expense", "top category", "where did my money go", "where is my money going",
                "spent the most", "spend the most"
            )
        ) {
            return ChatIntent.BiggestExpense(scope)
        }

        val matchedCategory = knownCategories.firstOrNull { cat -> cat.isNotBlank() && q.contains(cat) }
        if (matchedCategory != null && containsAny(q, "spend", "spent", "spending", "expense", "cost")) {
            return ChatIntent.CategorySpend(matchedCategory, scope)
        }

        if (containsAny(
                q, "how much did i spend", "how much have i spent", "how much i spent",
                "total spent", "total spend", "total expense"
            )
        ) {
            return ChatIntent.TotalSpend(scope)
        }

        return null
    }

    private fun detectScope(q: String): TimeScope = when {
        containsAny(q, "last month", "previous month") -> TimeScope.PREVIOUS_MONTH
        containsAny(q, "this week", "past week", "weekly", "week") -> TimeScope.THIS_WEEK
        else -> TimeScope.CURRENT_MONTH
    }

    private fun containsAny(text: String, vararg needles: String): Boolean =
        needles.any { text.contains(it) }
}
