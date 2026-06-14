package com.example.expense.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_budgets")
data class PendingBudgetEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val categoryId: String,
    val currency: String,
    val limitAmount: Int,
    val month: String,
    val createdAtLocal: Long = System.currentTimeMillis()
)
