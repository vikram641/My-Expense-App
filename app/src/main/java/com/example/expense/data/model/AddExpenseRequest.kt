package com.example.expense.data.model

data class AddExpenseRequest(
    val amount: Int=0,
    val categoryId: String = "",
    val currency: String = "",
    val date: String="",
    val note: String="",
    val receiptUrl: String = ""

)