package com.example.expense.data.model

data class Expense(
    val amount: Int,
    val category: Category,
    val createdAt: String,
    val currency: String,
    val date: String,
    val id: String,
    val note: String,
    val receiptUrl: Any,
    val updatedAt: String
)