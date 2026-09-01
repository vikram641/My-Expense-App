package com.example.expense.data.model

data class GetExpenseSyncDataResponse(
    val expenses: List<Expense>,
    val months: List<String>,
    val summary: List<Summary>
)

data class ExpenseXXX(
    val amount: Int,
    val category: CategoryXX,
    val createdAt: String,
    val currency: String,
    val date: String,
    val id: String,
    val note: String,
    val receiptUrl: Any,
    val updatedAt: String
)

data class Summary(
    val month: String,
    val total: Int
)