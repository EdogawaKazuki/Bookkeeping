package com.edogawakazuki.bookkeeping

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edogawakazuki.bookkeeping.data.database.entities.AccountEntity
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity
import com.edogawakazuki.bookkeeping.data.viewmodel.AccountFetchViewModel
import com.edogawakazuki.bookkeeping.data.viewmodel.TransactionFetchViewModel
import com.edogawakazuki.bookkeeping.utils.Utils

@Composable
fun AccountPage(
    activity: Activity,
    accountFetchViewModel: AccountFetchViewModel,
    onItemClick: (intent: Intent) -> Unit
) {
    // TODO: Not load all data, just load the first 10 transactions. load more when scrolling
    val accounts by accountFetchViewModel.accounts.observeAsState(listOf())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(accounts) { account ->
            AccountItem(activity, account, onItemClick)
        }
    }
}


@Composable
fun AccountItem(
    activity: Activity,
    account: AccountEntity,
    onItemClick: (intent: Intent) -> Unit)
{
    Column {
        Box(
            modifier = Modifier
                .clickable {
                    val intent = Intent(activity, EditTransactionActivity::class.java).apply {
                        putExtra("id", account.id)
                        putExtra("name", account.accountName)
                        putExtra("balance", account.balance)
                        putExtra("currency", account.currency)
                    }
                    onItemClick(intent)
                }
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Row {
                    Text(text = "Name: ${account.accountName}", modifier = Modifier.padding(end = 16.dp))
                    Text(text = "Balance: ${Utils.formatCurrency(account.balance, account.currency)}", modifier = Modifier.padding(end = 16.dp))
                }
                Row{
                    Text(text = "Currency: ${account.currency}", modifier = Modifier.padding(end = 16.dp))
                }
            }
        }
        HorizontalDivider()
    }
}