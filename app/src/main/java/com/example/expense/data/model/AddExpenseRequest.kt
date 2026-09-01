package com.example.expense.data.model

data class AddExpenseRequest(

    val amount: String,
    val categoryId: String = "",
    val currency: String = "",
    val date: String="",
    val note: String="",
    val receiptUrl: String = "",
    // changes

    val categoryName : String  = "",
    val categoryIcon : String = "",
    val categoryColor : String = "",
    val userId : String = ""

)