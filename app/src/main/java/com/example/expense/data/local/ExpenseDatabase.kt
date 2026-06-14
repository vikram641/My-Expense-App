package com.example.expense.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ExpenseEntity::class,
        PendingExpenseEntity::class,
        BudgetEntity::class,
        PendingBudgetEntity::class,
        CategoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun pendingExpenseDao(): PendingExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun pendingBudgetDao(): PendingBudgetDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DB_NAME = "expense.db"
    }
}
