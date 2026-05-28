package com.example.data.model

data class BudgetData(
    val categoryColor: String,
    val categoryId: String,
    val categoryName: String,
    val currency: String,
    val id: String,
    val limitAmount: Int,
    val month: String,
    val spentAmount: Int
)