package com.edogawakazuki.bookkeeping.data.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import com.edogawakazuki.bookkeeping.data.viewmodel.TransactionFetchViewModel

class TransactionFetchViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionFetchViewModel::class.java)) {
            return TransactionFetchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}