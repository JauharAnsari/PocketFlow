package com.example.pocketflow.data.dao

import androidx.room.*
import com.example.pocketflow.data.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE dateTime BETWEEN :start AND :end")
    fun getTransactionsByDateRange(start: Long, end: Long): Flow<List<Transaction>>

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}
