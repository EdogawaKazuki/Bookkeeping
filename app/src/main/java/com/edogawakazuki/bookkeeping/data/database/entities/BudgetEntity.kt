package com.edogawakazuki.bookkeeping.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val budgetAmount: Double,
    val startDate: Long, // Timestamp
    val endDate: Long, // Timestamp
    val accountId: Long, // Foreign key to AccountEntity
    val description: String // Budget purpose or name
)