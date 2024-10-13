package com.edogawakazuki.bookkeeping.data.database.dao

import androidx.room.*
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Long)
}