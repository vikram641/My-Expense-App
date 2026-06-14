package com.example.expense.data.local

import androidx.room.Entity

@Entity(tableName = "budgets", primaryKeys = ["categoryId", "month"])
data class BudgetEntity(
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val categoryColor: String,
    val currency: String,
    val limitAmount: Int,
    val month: String,
    val spentAmount: Int
)
