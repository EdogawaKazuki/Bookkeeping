package com.edogawakazuki.bookkeeping.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.edogawakazuki.bookkeeping.data.database.dao.AccountDao
import com.edogawakazuki.bookkeeping.data.database.dao.TagDao
import com.edogawakazuki.bookkeeping.data.database.dao.TransactionDao
import com.edogawakazuki.bookkeeping.data.database.entities.AccountEntity
import com.edogawakazuki.bookkeeping.data.database.entities.TagEntity
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity

// Remember to add entities to the database here
@Database(entities = [TransactionEntity::class, TagEntity::class, AccountEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun tagDao(): TagDao
    abstract fun accountDao(): AccountDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // SQL command to create the new "tags" table during migration
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `tags` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `type` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `color` INTEGER NOT NULL
            )
        """.trimIndent())
    }
}
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // SQL command to create the new "accounts" table during migration
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `accounts` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `accountName` TEXT NOT NULL,
                `balance` DOUBLE NOT NULL,
                `currency` TEXT NOT NULL
            )
        """.trimIndent())
    }
}
