package com.example.data.Api

import com.example.data.model.ExpenseX
import com.example.data.model.PaginationX

data class SearchApiData(
    val expenses: List<ExpenseX>,
    val pagination: PaginationX
)