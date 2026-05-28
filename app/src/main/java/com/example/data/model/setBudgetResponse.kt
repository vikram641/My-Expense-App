package com.example.data.model

data class setBudgetResponse(
    val categoryId: String,
    val id: String,
    val limitAmount: Int,
    val month: String
)