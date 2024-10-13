package com.edogawakazuki.bookkeeping.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionEditViewModel(private val transactionRepository: TransactionRepository):ViewModel() {

    fun createTransaction(transaction: TransactionEntity){
        viewModelScope.launch {
            transactionRepository.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: TransactionEntity){
//        viewModelScope.launch {
//            transactionRepository.updateTransaction(transaction)
//        }
    }

}