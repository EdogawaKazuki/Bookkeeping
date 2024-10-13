package com.edogawakazuki.bookkeeping.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionFetchViewModel(private val transactionRepository: TransactionRepository):ViewModel() {
    private val _transactions = MutableLiveData<List<TransactionEntity>>()

    val transactions: LiveData<List<TransactionEntity>> get() = _transactions

    init{
        loadTransactions()
    }

    fun loadTransactions(){
        viewModelScope.launch {
            _transactions.value = transactionRepository.getAllTransactions()
        }
    }
}