package com.example.expense.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local Room representation of an expense.
 *
 * The API's nested `CategoryX` is flattened into plain columns here, because
 * Room stores primitive columns. If you ever need the nested object back, the
 * mappers in ExpenseMappers.kt rebuild it.
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val amount: Int,
    val currency: String,
    val date: String,
    val note: String,
    val receiptUrl: String?,
    val createdAt: String,
    val updatedAt: String,

    // flattened category
    val categoryId: String,
    val categoryName: String,
    val categoryColor: String,
    val categoryIcon: String
)
