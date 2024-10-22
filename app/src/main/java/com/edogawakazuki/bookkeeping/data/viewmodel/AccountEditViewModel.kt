package com.edogawakazuki.bookkeeping.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edogawakazuki.bookkeeping.data.database.entities.AccountEntity
import com.edogawakazuki.bookkeeping.data.repository.AccountRepository
import kotlinx.coroutines.launch

class AccountEditViewModel(private val accountRepository: AccountRepository): ViewModel() {
    fun createAccount(account: AccountEntity){
        viewModelScope.launch {
           accountRepository.insertAccount(account)
       }
    }
    fun updateAccount(account: AccountEntity){
        viewModelScope.launch {
            accountRepository.updateAccount(account)
        }
    }

}