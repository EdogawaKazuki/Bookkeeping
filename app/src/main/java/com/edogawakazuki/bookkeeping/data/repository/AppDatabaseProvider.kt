package com.edogawakazuki.bookkeeping.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.edogawakazuki.bookkeeping.data.database.AppDatabase
import com.edogawakazuki.bookkeeping.data.database.MIGRATION_1_2
import com.edogawakazuki.bookkeeping.data.database.MIGRATION_2_3

class AppDatabaseProvider(context: Context) {
    val db: AppDatabase by lazy {
        Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "app-database"
    ).addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3)
        .build()
    }
}
