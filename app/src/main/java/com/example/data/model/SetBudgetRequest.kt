package com.example.data.model

data class SetBudgetRequest(
    val categoryId: String = "",
    val currency: String = "",
    val limitAmount: Int = 0,
    val month: String = ""
)