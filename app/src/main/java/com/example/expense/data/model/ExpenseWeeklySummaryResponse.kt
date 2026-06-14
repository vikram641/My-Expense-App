package com.example.expense.data.model

data class ExpenseWeeklySummaryResponse(
    val from: String,
    val to: String,
    val totalSpent: Int
)