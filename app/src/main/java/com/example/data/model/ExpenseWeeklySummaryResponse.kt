package com.example.data.model

data class ExpenseWeeklySummaryResponse(
    val from: String,
    val to: String,
    val totalSpent: Int
)