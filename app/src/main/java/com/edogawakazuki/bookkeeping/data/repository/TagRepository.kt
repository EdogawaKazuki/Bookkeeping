package com.edogawakazuki.bookkeeping.data.repository

import com.edogawakazuki.bookkeeping.data.database.dao.TagDao
import com.edogawakazuki.bookkeeping.data.database.entities.TagEntity

class TagRepository(private val tagDao: TagDao) {
    suspend fun getAllTags(): List<TagEntity> {
        return tagDao.getAllTags()
    }

    suspend fun insertTag(tag: TagEntity) {
        tagDao.insertTag(tag)
    }

    suspend fun deleteTag(tag: TagEntity) {
        tagDao.deleteTag(tag)
    }
}