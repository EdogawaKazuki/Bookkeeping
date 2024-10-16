package com.edogawakazuki.bookkeeping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.edogawakazuki.bookkeeping.data.repository.AppDatabaseProvider
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme
import com.edogawakazuki.bookkeeping.utils.Utils
import kotlinx.coroutines.launch
import java.util.Calendar


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
        var amount = intent.getDoubleExtra("amount", 0.0)
        var description = intent.getStringExtra("description") ?: ""
        var dateTime = intent.getLongExtra("date", System.currentTimeMillis())
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = dateTime
        val account = intent.getLongExtra("account", 0) ?: ""
        var category = intent.getStringExtra("category") ?: ""

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
                    var amountTextField = remember { mutableStateOf(TextFieldValue(amount.toString())) }
                    val descriptionTextField = remember { mutableStateOf(TextFieldValue(description)) }
                    val accountTextField = remember { mutableStateOf(TextFieldValue(account.toString())) }
                    val categoryTextField = remember { mutableStateOf(TextFieldValue(category)) }
                    // auto focus switch
                    val showDatePicker = remember { mutableStateOf(false) }
                    val showTimePicker = remember { mutableStateOf(false) }
                    val focusManager = LocalFocusManager.current
                    val (focusRequester1, focusRequester2, focusRequester3, focusRequester4) = FocusRequester.createRefs()

                    Column (
                        modifier = Modifier.padding(innerPadding)
                    ){
                        LaunchedEffect(Unit) {
                            focusRequester1.requestFocus()
                        }
                        TextField(
                            value = amountTextField.value,
                            onValueChange = {
                                text -> amountTextField.value = text
                                amount = text.text.toDouble()
                                            },
                            label = { Text("Amount") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester1)
                                .onFocusChanged { focusState ->
                                    Log.d("Focus", "Amount TextField focused: ${focusState.isFocused}")
                                    if(focusState.isFocused){
                                        val text = amountTextField.value.text
                                        amountTextField.value = amountTextField.value.copy(
                                            selection = TextRange(0, text.length)
                                        )
                                    }
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
                            value = descriptionTextField.value,
                            onValueChange = {
                                descriptionTextField.value = it
                                description = it.text
                                            },
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
                                onNext = {
                                    showDatePicker.value = true
                                    Log.d("EditTransactionActivity", "onNext")
                                }
                            )
                        )
                        HorizontalDivider()
                        DatePickerFieldToModal(
                            showModel = showDatePicker,
                            calendar = calendar,
                            onDateSelected = {
                                calendar = it
                                focusRequester4.requestFocus()
                                dateTime = calendar.timeInMillis
                            }
                        )
                        HorizontalDivider()
                        TimePickerFieldToModal(
                            showModel = showTimePicker,
                            calendar = calendar,
                            onTimeSelected = {
                                calendar = it
                                focusRequester4.requestFocus()
                                dateTime = calendar.timeInMillis
                            }
                        )
                        HorizontalDivider()
                        TextField(
                            value = categoryTextField.value,
                            onValueChange = {
                                categoryTextField.value = it
                                category = it.text
                                            },
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
                                    submitTransaction(id, amount, description, dateTime, category)
                                }
                            )
                        )
                        HorizontalDivider()
                        Button(
                            onClick = {
                                submitTransaction(id, amount, description, dateTime, category)
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
        if(id== (-1).toLong()){ // no id means insert a new transaction
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
        Toast.makeText(this, "amount: $amount, description: $description, date: $date, category: $category", Toast.LENGTH_SHORT).show()
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