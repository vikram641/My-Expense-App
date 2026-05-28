package com.example.data.model

data class ExpenseX(
    val amount: Int,
    val category: CategoryX,
    val createdAt: String,
    val currency: String,
    val date: String,
    val id: String,
    val note: String,
    val receiptUrl: String,
    val updatedAt: String
)