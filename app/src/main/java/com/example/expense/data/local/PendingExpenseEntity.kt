package com.example.expense.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An expense the user created while offline (or that couldn't reach the server).
 * Mirrors [com.example.data.model.AddExpenseRequest] plus a local id and a
 * timestamp so the queue drains in the order things were added (FIFO).
 */
@Entity(tableName = "pending_expenses")
data class PendingExpenseEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val amount: Int,
    val categoryId: String,
    val currency: String,
    val date: String,
    val note: String,
    val receiptUrl: String,
    val createdAtLocal: Long = System.currentTimeMillis()
)
