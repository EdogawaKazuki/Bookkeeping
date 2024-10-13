package com.edogawakazuki.bookkeeping.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val amount: Double,
    val description: String,
    val date: Long, // Stored as timestamp
    val accountId: Long, // Foreign key to AccountEntity
    val personId: Long? = null, // Foreign key to PersonEntity (nullable)
    val tag: String // For categorization
)
