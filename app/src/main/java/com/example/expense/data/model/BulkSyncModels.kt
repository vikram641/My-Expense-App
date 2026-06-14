package com.example.expense.data.model

data class BulkExpenseSyncRequest(val expenses: List<AddExpenseRequest>)

data class BulkBudgetSyncRequest(val budgets: List<SetBudgetRequest>)

data class BulkSyncResponse(
    val synced: Int = 0,
    val failed: Int = 0,
    val errors: List<String> = emptyList()
)
