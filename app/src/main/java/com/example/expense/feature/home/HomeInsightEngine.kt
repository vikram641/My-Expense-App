package com.example.expense.feature.home

import com.example.expense.core.UiState
import com.example.expense.data.local.ExpenseEntity
import com.example.expense.data.repository.Repository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val BASELINE_WEEKS = 3
private const val MIN_PERCENT_INCREASE = 20

/**
 * Purely computed from local Room data - no AI call, so it's free, instant, and
 * safe to run on every Home load without touching Gemini/Groq quota at all.
 * Returns null whenever there isn't a real signal (not enough history, spending
 * flat/down, on track within budget) rather than show a fabricated insight.
 */
object HomeInsightEngine {

    suspend fun compute(repository: Repository): HomeInsight? {
        val now = Calendar.getInstance()
        val daysInMonth = now.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayOfMonth = now.get(Calendar.DAY_OF_MONTH)
        val currentMonthKey = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(now.time)

        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFmt.format(now.time)

        val weekStartCal = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -6) }
        val currentWeekExpenses = repository.getExpensesForExportRange(dateFmt.format(weekStartCal.time), today)

        val baselineEndCal = (weekStartCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
        val baselineStartCal = (baselineEndCal.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, -(BASELINE_WEEKS * 7 - 1))
        }
        val baselineExpenses = repository.getExpensesForExportRange(
            dateFmt.format(baselineStartCal.time),
            dateFmt.format(baselineEndCal.time)
        )

        val categoryTrend = biggestCategoryIncrease(currentWeekExpenses, baselineExpenses)

        val monthExpenses = repository.getExpensesForExportMonth(currentMonthKey)
        val totalSpent = monthExpenses.sumOf { it.amount.toDoubleOrNull()?.toInt() ?: 0 }
        val budgetResult = repository.getBudgets(currentMonthKey)
        val totalBudget = (budgetResult as? UiState.Success)?.data?.data?.sumOf { it.limitAmount } ?: 0

        val projectedOverageDay = projectOverageDay(totalSpent, totalBudget, dayOfMonth, daysInMonth)

        if (categoryTrend == null && projectedOverageDay == null) return null

        return HomeInsight(
            categoryName = categoryTrend?.first,
            categoryPercentChange = categoryTrend?.second,
            projectedOverageDay = projectedOverageDay,
            daysInMonth = daysInMonth,
            dayOfMonth = dayOfMonth
        )
    }

    private fun projectOverageDay(totalSpent: Int, totalBudget: Int, dayOfMonth: Int, daysInMonth: Int): Int? {
        if (totalBudget <= 0 || totalSpent <= 0 || dayOfMonth <= 0) return null
        val dailyRate = totalSpent.toDouble() / dayOfMonth
        val crossoverDay = totalBudget / dailyRate
        return if (crossoverDay > dayOfMonth && crossoverDay < daysInMonth) crossoverDay.toInt() else null
    }

    private fun biggestCategoryIncrease(
        current: List<ExpenseEntity>,
        baseline: List<ExpenseEntity>
    ): Pair<String, Int>? {
        val currentByCategory = current.groupBy { it.categoryName }
            .mapValues { (_, list) -> list.sumOf { it.amount.toDoubleOrNull()?.toInt() ?: 0 } }
        val baselineWeeklyAverage = baseline.groupBy { it.categoryName }
            .mapValues { (_, list) -> list.sumOf { it.amount.toDoubleOrNull()?.toInt() ?: 0 } / BASELINE_WEEKS }

        return currentByCategory.mapNotNull { (name, amount) ->
            val average = baselineWeeklyAverage[name] ?: return@mapNotNull null
            if (average <= 0) return@mapNotNull null
            val percentChange = ((amount - average) * 100.0 / average).toInt()
            if (percentChange >= MIN_PERCENT_INCREASE) name to percentChange else null
        }.maxByOrNull { it.second }
    }
}
