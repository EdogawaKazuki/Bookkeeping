package com.edogawakazuki.bookkeeping.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edogawakazuki.bookkeeping.data.database.entities.AccountEntity
import com.edogawakazuki.bookkeeping.data.repository.AccountRepository
import kotlinx.coroutines.launch

class AccountFetchViewModel(private val accountRepository: AccountRepository): ViewModel() {
    private val _accounts = MutableLiveData<List<AccountEntity>>()

    val accounts: LiveData<List<AccountEntity>> get() = _accounts

    init{
        loadAccounts()
    }

    fun loadAccounts(){
        viewModelScope.launch {
            _accounts.value = accountRepository.getAllAccounts()
        }
    }

}