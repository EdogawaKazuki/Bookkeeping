package com.edogawakazuki.bookkeeping.data.repository

import com.edogawakazuki.bookkeeping.data.database.dao.AccountDao
import com.edogawakazuki.bookkeeping.data.database.entities.AccountEntity

class AccountRepository(private val accountDao: AccountDao) {
    suspend fun getAllAccounts(): List<AccountEntity> {
        return accountDao.getAllAccounts()
    }

    suspend fun insertAccount(account: AccountEntity) {
        accountDao.insertAccount(account)
    }

    suspend fun updateAccount(account: AccountEntity) {
        accountDao.updateAccount(account)
    }

    suspend fun deleteAccountById(accountId: Long) {
        accountDao.deleteAccount(accountId)
    }
}