package com.example.expense.data.model

data class Expense(
    val amount: Int,
    val category: Category,
    val createdAt: String,
    val currency: String,
    val date: String,
    val id: String,
    val note: String,

    // receiptUrl type change ANY to string
    val receiptUrl: Any,
    val updatedAt: String
)

