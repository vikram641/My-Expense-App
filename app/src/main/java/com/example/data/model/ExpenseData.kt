package com.example.data.model

import com.example.data.model.Pagination

data class ExpenseData(
    val expenses: List<Expense>,
    val pagination: Pagination
)