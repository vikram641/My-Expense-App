package com.example.expense.data.model

data class ExpenseSynceDataResponse(
    val failed: Int,
    val results: List<Result>,
    val synced: Int,
    val total: Int
)