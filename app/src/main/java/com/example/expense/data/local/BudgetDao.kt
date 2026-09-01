package com.example.expense.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(
        budget: BudgetEntity
    )

    @Query("SELECT * FROM budgets WHERE month = :month")
    fun observeBudgets(month: String): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE month = :month")
    suspend fun getBudgets(month: String?): List<BudgetEntity>

    @Query("SELECT * FROM budgets")
    suspend fun getAllBudget(): List<BudgetEntity>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBudgets(budgets: List<BudgetEntity>)

    @Query("DELETE FROM budgets WHERE categoryId = :categoryId AND month = :month")
    suspend fun deleteBudget(categoryId: String, month: String)

    @Query("DELETE FROM budgets WHERE month = :month")
    suspend fun clearForMonth(month: String)

    @Query("DELETE FROM budgets")
    suspend fun clearAll()
}
