package com.example.expense.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingBudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PendingBudgetEntity): Long

    @Query("SELECT * FROM pending_budgets ORDER BY createdAtLocal ASC")
    suspend fun getAll(): List<PendingBudgetEntity>

    @Query("SELECT COUNT(*) FROM pending_budgets")
    fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM pending_budgets")
    suspend fun count(): Int

    @Query("DELETE FROM pending_budgets WHERE localId = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_budgets")
    suspend fun clearAll()
}
