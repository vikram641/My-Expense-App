package com.example.expense.data.model

data class ExpenseQuery(
    val page: Int = 1,
    val limit: Int = 20,
    val category: String? = null,
    val from: String? = null,
    val to: String? = null,
    val search: String? = null
)