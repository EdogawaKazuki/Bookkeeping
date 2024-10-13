package com.edogawakazuki.bookkeeping.data.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edogawakazuki.bookkeeping.data.repository.TagRepository
import com.edogawakazuki.bookkeeping.data.viewmodel.TagViewModel

class TagViewModelFactory(private val repository: TagRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            return TagViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}