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
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity
import com.edogawakazuki.bookkeeping.data.viewmodel.TransactionFetchViewModel
import com.edogawakazuki.bookkeeping.utils.Utils

@Composable
fun TransactionPage(
    activity: Activity,
    transactionFetchViewModel: TransactionFetchViewModel,
    onItemClick: (intent: Intent) -> Unit
) {
    // TODO: Not load all data, just load the first 10 transactions. load more when scrolling
    val transactions by transactionFetchViewModel.transactions.observeAsState(listOf())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(activity, transaction, onItemClick)
        }
    }
}


@Composable
fun TransactionItem(
    activity: Activity,
    transaction: TransactionEntity,
    onItemClick: (intent: Intent) -> Unit)
{
    Column {
        Box(
            modifier = Modifier
                .clickable {
                    val intent = Intent(activity, EditTransactionActivity::class.java).apply {
                        putExtra("id", transaction.id)
                        putExtra("amount", transaction.amount)
                        putExtra("description", transaction.description)
                        putExtra("date", transaction.date)
                        putExtra("account", transaction.accountId)
                        putExtra("category", transaction.tag)
                    }
                    onItemClick(intent)
                }
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Row {
                    Text(text = "Amount: ${transaction.amount}", modifier = Modifier.padding(end = 16.dp))
                    Text(text = "Date: ${Utils.convertMillisToDateString(transaction.date)}", modifier = Modifier.padding(end = 16.dp))
                }
                Row{
                    Text(text = "Description: ${transaction.description}", modifier = Modifier.padding(end = 16.dp))
                    Text(text = "Account: ${transaction.accountId}", modifier = Modifier.padding(end = 16.dp))
                }
            }
        }
        HorizontalDivider()
    }
}