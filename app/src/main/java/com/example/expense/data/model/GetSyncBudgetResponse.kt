package com.example.expense.data.model

data class GetSyncBudgetResponse(
    val budgets: List<BudgetX>,
    val months: List<String>
)