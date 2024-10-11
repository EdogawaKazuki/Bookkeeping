package com.edogawakazuki.bookkeeping.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "receivables",
    foreignKeys = [
        ForeignKey(entity = PersonEntity::class, parentColumns = ["id"], childColumns = ["personId"]),
        ForeignKey(entity = TransactionEntity::class, parentColumns = ["id"], childColumns = ["transactionId"])
    ],
    indices = [Index("personId"), Index("transactionId")]
)
data class ReceivableEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val dueDate: Long, // Timestamp for due date
    val interestRate: Double = 0.0, // Interest on the receivable
    val personId: Long, // Foreign key to PersonEntity
    val transactionId: Long // Foreign key to TransactionEntity
)
