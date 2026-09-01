package com.example.expense.feature.home

/**
 * A computed (not AI-generated) spending insight for the Home AI Insight card.
 * Every field here is either real derived data or null/absent - never a filler
 * value - so HomeInsightEngine returns null entirely rather than fabricate one
 * when there isn't a genuine signal to report.
 */
data class HomeInsight(
    val categoryName: String?,
    val categoryPercentChange: Int?,
    val projectedOverageDay: Int?,
    val daysInMonth: Int,
    val dayOfMonth: Int
) {
    val hasCategoryTrend: Boolean get() = categoryName != null && categoryPercentChange != null
    val hasForecast: Boolean get() = projectedOverageDay != null

    val monthProgressFraction: Float get() = dayOfMonth.toFloat() / daysInMonth
    val overageFraction: Float? get() = projectedOverageDay?.let { it.toFloat() / daysInMonth }
}
