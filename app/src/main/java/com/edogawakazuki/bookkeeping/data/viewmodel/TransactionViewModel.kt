package com.edogawakazuki.bookkeeping.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {

    // private MutableLiveData to update the data internally
    private val _transactions =MutableLiveData<List<TransactionEntity>>()

    // Public LiveData to expose to the UI
    val transactions: LiveData<List<TransactionEntity>> get() = _transactions

    fun loadTransactions() {
        viewModelScope.launch {
            _transactions.value = transactionRepository.getAllTransactions()
        }
//        _transactions.value = transactionRepository.getAllTransactions()
    }

    fun insertTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.insertTransaction(transaction)
            loadTransactions()
        }
//        transactionRepository.insertTransaction(transaction)
    }
}