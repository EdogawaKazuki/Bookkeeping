package com.edogawakazuki.bookkeeping.data.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edogawakazuki.bookkeeping.data.repository.AccountRepository
import com.edogawakazuki.bookkeeping.data.viewmodel.AccountFetchViewModel

class AccountFetchViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountFetchViewModel::class.java)) {
            return AccountFetchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}