package com.example.data.model

data class ExpenseSummaryData(
    val byCategory: List<ByCategory>,
    val byMonth: List<ByMonth>,
    val currency: String,
    val totalBudget: Int,
    val totalSpent: Int
)