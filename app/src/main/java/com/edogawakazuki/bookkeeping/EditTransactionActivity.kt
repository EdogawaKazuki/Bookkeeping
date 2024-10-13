package com.edogawakazuki.bookkeeping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.edogawakazuki.bookkeeping.data.repository.AppDatabaseProvider
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme
import kotlinx.coroutines.launch


//Todo: Baidu IME not support the action.next

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
class EditTransactionActivity : ComponentActivity() {
    private val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(AppDatabaseProvider(this@EditTransactionActivity).db.transactionDao())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val id = intent.getLongExtra("id", -1)
        val amount = intent.getDoubleExtra("amount", 0.0)
        val description = intent.getStringExtra("description") ?: ""
        val date = intent.getLongExtra("date", 0)
        val account = intent.getStringExtra("account") ?: ""
        val category = intent.getStringExtra("category") ?: ""

        setContent {
            BookkeepingTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Bookkeeping") },
                            actions = {
                                Button(onClick = {
                                    val resultIntent = Intent().apply {
                                        putExtra("action", "delete")
                                    }
                                    lifecycleScope.launch {
                                        transactionRepository.deleteTransactionById(id)
                                    }
                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()
                                }) {
                                    Text("Delete")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    val id = remember { mutableLongStateOf(id) }
                    val amount = remember { mutableDoubleStateOf(amount) }
                    val description = remember { mutableStateOf(description) }
                    val date = remember { mutableLongStateOf(date) }
                    val account = remember { mutableStateOf(account) }
                    val category = remember { mutableStateOf(category) }
                    // auto focus switch
                    val focusManager = LocalFocusManager.current
                    val (focusRequester1, focusRequester2, focusRequester3, focusRequester4) = FocusRequester.createRefs()

                    Column (
                        modifier = Modifier.padding(innerPadding)
                    ){
                        LaunchedEffect(Unit) {
                            focusRequester1.requestFocus()
                        }
                        TextField(
                            value = amount.doubleValue.toString(),
                            onValueChange = { amount.doubleValue = it.toDouble() },
                            label = { Text("Amount") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester1)
                                .onFocusChanged { focusState ->
                                    Log.d("Focus", "Amount TextField focused: ${focusState.isFocused}")
                                },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusRequester2.requestFocus()
                                    Log.d("EditTransactionActivity", "onNext")
                                }
                            )
                        )
                        HorizontalDivider()
                        TextField(
                            value = description.value,
                            onValueChange = { description.value = it },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester2)
                                .onFocusChanged { focusState ->
                                    Log.d("Focus", "Amount TextField focused: ${focusState.isFocused}")
                                },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequester3.requestFocus() }
                            )
                        )
                        HorizontalDivider()
                        TextField(
                            value = date.longValue.toString(),
                            onValueChange = { date.longValue = it.toLong() },
                            label = { Text("Date") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester3)
                                .onFocusChanged { focusState ->
                                    Log.d("Focus", "Amount TextField focused: ${focusState.isFocused}")
                                },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusRequester4.requestFocus() }
                            )
                        )
                        HorizontalDivider()
                        TextField(
                            value = category.value,
                            onValueChange = { category.value = it },
                            label = { Text("Category") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester4)
                                .onFocusChanged { focusState ->
                                    Log.d("Focus", "Amount TextField focused: ${focusState.isFocused}")
                                },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus()
                                    Log.d("EditTransactionActivity", "onDone")
                                    submitTransaction(id.value, amount.value, description.value, date.value, category.value)
                                }
                            )
                        )
                        HorizontalDivider()
                        Button(
                            onClick = {
                                submitTransaction(id.value, amount.value, description.value, date.value, category.value)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Submit")
                        }
                    }

                }
            }
        }

    }

    private fun submitTransaction(id: Long, amount: Double, description: String, date: Long, category: String) {

        val transactionEntity = com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity(
            amount = amount,
            description = description,
            date = date,
            accountId = 0,
            tag = category
        )
        var action = ""
        if(id== (-1).toLong()){
            lifecycleScope.launch {
                transactionRepository.insertTransaction(transactionEntity)
            }
            action = "insert"
        }else{
            transactionEntity.id = id
            lifecycleScope.launch {
                transactionRepository.updateTransaction(transactionEntity)
            }
            action = "update"
        }
        val resultIntent = Intent().apply {
            putExtra("action", action)
            putExtra("amount", amount)
            putExtra("description", description)
            putExtra("date", date)
            putExtra("category", category)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookkeepingTheme {
        Greeting("Android")
    }
}