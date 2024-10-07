package com.edogawakazuki.bookkeeping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme

@OptIn(ExperimentalMaterial3Api::class)
class EditTransactionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookkeepingTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Bookkeeping") },
                            actions = {
                                Button(onClick = { /* enter activ */ }) {
                                    Text("Try me")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column {
                        TextField(
                            value = "",
                            onValueChange = { },
                            label = { Text("Amount") },
                            modifier = Modifier.padding(innerPadding)
                        )
                        TextField(
                            value = "",
                            onValueChange = { },
                            label = { Text("Description") },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                }
            }
        }
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