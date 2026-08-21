package com.example.expense.data.model

data class Budget(
    val categoryColor: String,
    val categoryIcon: String,
    val categoryId: String,
    val categoryName: String,
    val currency: String,
    val id: String,
    val limitAmount: Int,
    val month: String,
    val spentAmount: Int
)