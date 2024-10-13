package com.edogawakazuki.bookkeeping.data.database.dao

import androidx.room.*
import com.edogawakazuki.bookkeeping.data.database.entities.TagEntity

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity)

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Delete
    suspend fun deleteTag(tag: TagEntity)

}