package com.example.data.model

data class AddExpenseResponse(
    val amount: String?,
    val categoryId: String? = "",
    val createdAt: String? = " ",
    val currency: String? = "",
    val date: String?,
    val id: String?,
    val note: String?
)