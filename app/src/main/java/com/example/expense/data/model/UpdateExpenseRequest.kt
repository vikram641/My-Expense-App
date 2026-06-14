package com.example.expense.data.model

data class UpdateExpenseRequest(
    val amount: Int? = null,
    val categoryId: String? = null,
    val currency: String? = null,
    val note: String? = null,
    val date: String? = null,
    val receiptUrl: String? = null
)
