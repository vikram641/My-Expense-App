package com.example.expense.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingExpenseDao {

    @Insert
    suspend fun insert(item: PendingExpenseEntity): Long

    /** FIFO order — oldest queued first, so syncs in the order created. */
    @Query("SELECT * FROM pending_expenses ORDER BY createdAtLocal ASC")
    suspend fun getAll(): List<PendingExpenseEntity>

    @Query("SELECT * FROM pending_expenses ORDER BY createdAtLocal ASC")
    fun observeAll(): Flow<List<PendingExpenseEntity>>

    /** For a "N pending" badge in the UI. */
    @Query("SELECT COUNT(*) FROM pending_expenses")
    fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM pending_expenses")
    suspend fun count(): Int

    @Query("DELETE FROM pending_expenses WHERE localId = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_expenses")
    suspend fun clearAll()
}
