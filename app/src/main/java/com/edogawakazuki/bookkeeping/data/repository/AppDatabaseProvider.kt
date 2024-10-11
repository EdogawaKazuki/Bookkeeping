package com.edogawakazuki.bookkeeping.data.repository

import android.content.Context
import androidx.room.Room
import com.edogawakazuki.bookkeeping.data.database.AppDatabase

class AppDatabaseProvider(context: Context) {
    val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "app-database"
    ).build()
}