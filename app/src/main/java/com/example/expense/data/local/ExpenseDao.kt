package com.example.expense.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    /**
     * Reactive stream of all cached expenses. Room re-emits automatically
     * whenever the table changes, so the UI updates itself after a refresh.
     * Final sorting/grouping is left to Utils.mapExpenses (dates are mixed
     * string formats), so a simple ORDER BY is fine here.
     */
    @Query("SELECT * FROM expenses ORDER BY updatedAt DESC")
    fun observeExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY updatedAt DESC")
    suspend fun getExpensesOnce(): List<ExpenseEntity>

    /** Insert or replace a whole batch (full-row upsert via REPLACE on PK). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExpenses(expenses: List<ExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun count(): Int
}
