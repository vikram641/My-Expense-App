package com.example.expense.data.model

import com.example.expense.data.model.ExpenseX
import com.example.expense.data.model.PaginationX

data class SearchApiData(
    val expenses: List<ExpenseX>,
    val pagination: PaginationX
)