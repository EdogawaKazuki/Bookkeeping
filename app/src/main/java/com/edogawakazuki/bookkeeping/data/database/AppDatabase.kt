package com.edogawakazuki.bookkeeping.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edogawakazuki.bookkeeping.data.database.dao.TransactionDao
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}