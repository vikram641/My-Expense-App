package com.example.expense.data.model

data class getSyncBudgetResponse(
    val budgets: List<BudgetX>,
    val months: List<String>
)