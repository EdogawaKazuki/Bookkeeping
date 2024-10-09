package com.edogawakazuki.bookkeeping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme

data class Transaction(
    val amount: Double,
    val description: String,
    val date: String,
    val category: String,
)

val transactions = mutableStateListOf(
    Transaction(100.0, "Lunch", "2021-10-01", "Food"),
    Transaction(200.0, "Dinner", "2021-10-02", "Food"),
    Transaction(300.0, "Breakfast", "2021-10-03", "Food"),
    Transaction(400.0, "Lunch", "2021-10-04", "Food"),
    Transaction(500.0, "Dinner", "2021-10-05", "Food"),
    Transaction(600.0, "Breakfast", "2021-10-06", "Food"),
    Transaction(700.0, "Lunch", "2021-10-07", "Food"),
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookkeepingTheme {
                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar(
                                title = { Text("Bookkeeping") },
                                actions = {
                                    Button(onClick = { /* enter activ */ }) {
                                        Text("Try me")
                                    }
                                }
                            )

                            TabBar()
                        }
                    },
                    floatingActionButton = {
                        Column {
                            FloatingActionButton (onClick = {
                                val intent = Intent(this@MainActivity, EditTransactionActivity::class.java)
                                editTransactionLauncher.launch(intent)
                            } ) {
                                Text("+")
                            }

                        }
                    },
                    content = { innerPadding ->
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                        ) {
                            Greeting(
                                name = "Kazuki",
                            )
                        }
                    }
                )
            }
        }
    }

    val editTransactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val amount = data?.getStringExtra("amount")
            val description = data?.getStringExtra("description")
            val date = data?.getStringExtra("date")
            val category = data?.getStringExtra("category")
            Toast.makeText(this, "Amount: $amount, Description: $description, Date: $date, Category: $category", Toast.LENGTH_SHORT).show()
            transactions.add(Transaction(amount!!.toDouble(), description!!, date!!, category!!))
        }
    }


    @Composable
    fun TabBar() {
        // State to keep track of the selected tab index
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        // Define the tabs
        val tabs = listOf("Home", "Transactions", "Budgets")
        val pageState = rememberPagerState{tabs.size}
        LaunchedEffect(selectedTabIndex) {
//            pageState.animateScrollToPage(selectedTabIndex)
            pageState.scrollToPage(selectedTabIndex)
        }

        LaunchedEffect(pageState.currentPage, pageState.interactionSource) {
            selectedTabIndex = pageState.currentPage
        }


        // TabRow to show the tabs
        Column {
            PrimaryScrollableTabRow (
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Iterate through each tab and create a Tab item
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pageState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> HomePage()
                    1 -> TransactionPage()
                    2 -> SettingsContent()
                }
            }
        }
    }

    @Composable
    fun HomePage() {
    }

    @Composable
    fun TransactionItem(transaction: Transaction) {
        Column {
            Row {
                Text(text = "Amount: ${transaction.amount}", modifier = Modifier.padding(end = 16.dp))
                Text(text = "Date: ${transaction.date}", modifier = Modifier.padding(end = 16.dp))
            }
            Row{
                Text(text = "Description: ${transaction.description}", modifier = Modifier.padding(end = 16.dp))
                Text(text = "Category: ${transaction.category}", modifier = Modifier.padding(end = 16.dp))
            }
            HorizontalDivider()
        }
    }

    @Composable
    fun TransactionPage() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }

    @Composable
    fun SettingsContent() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings Screen")
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
}