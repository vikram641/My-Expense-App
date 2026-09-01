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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulkExpenses(expense: List<ExpenseEntity>)

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<ExpenseEntity>


    @Query("""
    SELECT * FROM expenses
    WHERE categoryId = :catId
    ORDER BY createdAt DESC
    """)
    suspend fun getExpensesById(catId: String): List<ExpenseEntity>


    @Query("""SELECT * FROM expenses WHERE (:month IS NULL OR strftime('%m', date) = :month)""")
    suspend fun getExpenses(month: String?): List<ExpenseEntity>

    @Query("""
        SELECT * FROM expenses
        WHERE strftime('%Y-%m', date) = :month
        ORDER BY date DESC
    """)
    suspend fun getMonthlyExpenses(
        month: String?
    ): List<ExpenseEntity>

    /** Expenses across a trailing range of months (inclusive), used for multi-month trend charts. */
    @Query("""
        SELECT * FROM expenses
        WHERE strftime('%Y-%m', date) BETWEEN :fromMonth AND :toMonth
        ORDER BY date ASC
    """)
    suspend fun getExpensesBetweenMonths(fromMonth: String, toMonth: String): List<ExpenseEntity>

    /** Expenses within an explicit [fromDate, toDate] range (inclusive), both yyyy-MM-dd -
     * used for the Settings > Export Data custom-range option. Plain string BETWEEN works
     * since yyyy-MM-dd sorts lexicographically the same as chronologically. */
    @Query("""
        SELECT * FROM expenses
        WHERE date BETWEEN :fromDate AND :toDate
        ORDER BY date ASC
    """)
    suspend fun getExpensesBetweenDates(fromDate: String, toDate: String): List<ExpenseEntity>


    @Query("SELECT * FROM expenses ORDER BY updatedAt DESC")
    fun observeExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY updatedAt DESC")
    suspend fun getExpensesOnce(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: String): ExpenseEntity?

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM expenses WHERE categoryId = :catId")
    suspend fun getExpensesByCatId(catId: String): List<ExpenseEntity>

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
