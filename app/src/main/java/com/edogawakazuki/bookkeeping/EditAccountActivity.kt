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
import com.edogawakazuki.bookkeeping.data.repository.AccountRepository
import com.edogawakazuki.bookkeeping.data.repository.AppDatabaseProvider
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch


//Todo: Baidu IME not support the action.next

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
class EditAccountActivity : ComponentActivity() {
    private val accountRepository: AccountRepository by lazy {
        AccountRepository(AppDatabaseProvider(this@EditAccountActivity).db.accountDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val id = intent.getLongExtra("id", -1)
        var accountName = intent.getStringExtra("accountName") ?: ""
        var balance = intent.getDoubleExtra("balance", 0.0)
        var currency = intent.getStringExtra("currency") ?: ""


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
                                        accountRepository.deleteAccountById(id)
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
                    var accountNameTextField =
                        remember { mutableStateOf(TextFieldValue(accountName)) }
                    var balanceTextField =
                        remember { mutableStateOf(TextFieldValue(balance.toString())) }
                    var currencyTextField = remember { mutableStateOf(TextFieldValue(currency)) }

                    val focusManager = LocalFocusManager.current
                    val (focusRequester1, focusRequester2, focusRequester3, focusRequester4) = FocusRequester.createRefs()

                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        LaunchedEffect(Unit) {
                            focusRequester1.requestFocus()
                        }
                        TextField(
                            value = accountNameTextField.value,
                            onValueChange = { text ->
                                accountNameTextField.value = text
                                accountName = text.text
                            },
                            label = { Text("Account Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester1)
                                .onFocusChanged { focusState ->
                                    Log.d(
                                        "Focus",
                                        "Amount TextField focused: ${focusState.isFocused}"
                                    )
                                    if (focusState.isFocused) {
                                        val text = accountNameTextField.value.text
                                        accountNameTextField.value =
                                            accountNameTextField.value.copy(
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
                                    Log.d("EditAccount", "onNext")
                                }
                            )
                        )
                        HorizontalDivider()
                        TextField(
                            value = balanceTextField.value,
                            onValueChange = {
                                balanceTextField.value = it
                                balance = it.text.toDouble()
                            },
                            label = { Text("Balance") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester2)
                                .onFocusChanged { focusState ->
                                    Log.d(
                                        "Focus",
                                        "Amount TextField focused: ${focusState.isFocused}"
                                    )
                                },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusRequester3.requestFocus()
                                    Log.d("EditAccount", "onNext")
                                }
                            )
                        )
                        HorizontalDivider()
                        TextField(
                            value = currencyTextField.value,
                            onValueChange = {
                                currencyTextField.value = it
                                currency = it.text
                            },
                            label = { Text("Category") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester4)
                                .onFocusChanged { focusState ->
                                    Log.d(
                                        "Focus",
                                        "Amount TextField focused: ${focusState.isFocused}"
                                    )
                                },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    Log.d("EditAccount", "onDone")
                                    submitAccount(id, accountName, balance, currency)
                                }
                            )
                        )
                        HorizontalDivider()
                        Button(
                            onClick = {
                                submitAccount(id, accountName, balance, currency)
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

    private fun submitAccount(id: Long, accountName: String, balance: Double, currency: String) {

        val accountEntity = com.edogawakazuki.bookkeeping.data.database.entities.AccountEntity(
            accountName = accountName,
            balance = balance,
            currency = currency
        )
        var action = ""
        if (id == (-1).toLong()) { // no id means insert a new account
            lifecycleScope.launch {
                accountRepository.insertAccount(accountEntity)
            }
            action = "insert"
        } else {
            accountEntity.id = id
            lifecycleScope.launch {
                accountRepository.updateAccount(accountEntity)
            }
            action = "update"
        }
        val resultIntent = Intent().apply {
            putExtra("action", action)
            putExtra("accountName", accountName)
            putExtra("balance", balance)
            putExtra("currency", currency)
        }
        Toast.makeText(
            this,
            "accountName: $accountName, balance: $balance, currency: $currency",
            Toast.LENGTH_SHORT
        ).show()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}