package com.example.expense.data.model

data class ExpenseSummaryResponse(
    val `data`: ExpenseSummaryData,
    val success: Boolean
)

data class ExpenseSummaryData(
    val byCategory: List<ByCategory>,
    val byMonth: List<ByMonth>,
    val currency: String,
    val totalBudget: Int,
    val totalSpent: Int
)

data class ByCategory(
    val amount: Int ,
    val categoryColor: String ,
    val categoryId: String,
    val categoryName: String,
    val percentage: Int
)

data class ByMonth(
    val amount: Int,
    val month: String
)