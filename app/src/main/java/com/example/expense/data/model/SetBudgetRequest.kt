package com.example.expense.data.model

data class SetBudgetRequest(
    val categoryId: String = "",
    val currency: String = "",
    val limitAmount: Int = 0,
    val month: String = "",
    // changes
    val categoryName : String = "",
    val categoryColor : String = "",
    val spendAmount : Int = 0,
    val categoryIcon : String = ""

)