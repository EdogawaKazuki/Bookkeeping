package com.edogawakazuki.bookkeeping.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edogawakazuki.bookkeeping.data.database.entities.TagEntity
import com.edogawakazuki.bookkeeping.data.repository.TagRepository
import kotlinx.coroutines.launch

class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {
    private val _tags = MutableLiveData<List<TagEntity>>()

    val tags: MutableLiveData<List<TagEntity>> get() = _tags

    init {
        loadTags()
    }

    private fun loadTags() {
        viewModelScope.launch {
            _tags.value = tagRepository.getAllTags()
        }
    }

    fun insertTag(tag: TagEntity) {
        viewModelScope.launch {
            tagRepository.insertTag(tag)
        }
    }

    fun deleteTag(tag: TagEntity) {
        viewModelScope.launch {
            tagRepository.deleteTag(tag)
        }
    }
}