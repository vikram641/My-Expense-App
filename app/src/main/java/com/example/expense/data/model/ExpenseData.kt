package com.example.expense.data.model

import com.example.expense.data.model.Pagination

data class ExpenseData(
    val expenses: List<Expense>,
    val pagination: Pagination
)