package com.edogawakazuki.bookkeeping.data.repository

import com.edogawakazuki.bookkeeping.data.database.dao.TransactionDao
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity


class TransactionRepository(private val transactionDao : TransactionDao) {
    suspend fun getAllTransactions(): List<TransactionEntity> {
        return transactionDao.getAllTransactions()
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransactionById(transactionId: Long) {
        transactionDao.deleteTransactionById(transactionId)
    }
}