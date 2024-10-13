package com.edogawakazuki.bookkeeping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelInitializer
import com.edogawakazuki.bookkeeping.data.database.entities.TransactionEntity
import com.edogawakazuki.bookkeeping.data.repository.AppDatabaseProvider
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import com.edogawakazuki.bookkeeping.data.viewmodel.TransactionFetchViewModel
import com.edogawakazuki.bookkeeping.data.viewmodelFactory.TransactionFetchViewModelFactory
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme

private lateinit var transactionFetchViewModel: TransactionFetchViewModel

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


        val transactionRepository = TransactionRepository(AppDatabaseProvider(this).db.transactionDao())
        val transactionFactory = TransactionFetchViewModelFactory(transactionRepository)
        transactionFetchViewModel = ViewModelProvider(this, transactionFactory)[TransactionFetchViewModel::class.java]

    }

    private val editTransactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val action: String? = data?.getStringExtra("action")
            val amount = data?.getDoubleExtra("amount", 0.0)
            val description = data?.getStringExtra("description")
            val date = data?.getLongExtra("date", 0)
            val accountId = data?.getLongExtra("account", 0)
            val tag = data?.getStringExtra("category")
            var toastMsg = "Amount: $amount, Description: $description, Date: $date, Category: $tag"
            if(action == "insert"){
                toastMsg = "Insert: $toastMsg"
            }else if(action == "update"){
                toastMsg = "Update: $toastMsg"
            }else if(action == "delete"){
                toastMsg = "Transaction deleted"
            }else{
                toastMsg = "Unknown action $action"
            }
            Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
            transactionFetchViewModel.loadTransactions()
        }

    }


    @Composable
    fun TabBar() {
        // State to keep track of the selected tab index
        var selectedTabIndex by remember { mutableIntStateOf(1) }

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
    fun TransactionItem(transaction: TransactionEntity) {
        Column {
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, EditTransactionActivity::class.java).apply {
                        putExtra("id", transaction.id)
                        putExtra("amount", transaction.amount)
                        putExtra("description", transaction.description)
                        putExtra("date", transaction.date)
                        putExtra("account", transaction.accountId)
                        putExtra("category", transaction.tag)
                    }
                    editTransactionLauncher.launch(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Text(text = "Amount: ${transaction.amount}", modifier = Modifier.padding(end = 16.dp))
                    Text(text = "Date: ${transaction.date}", modifier = Modifier.padding(end = 16.dp))
                }
                Row{
                    Text(text = "Description: ${transaction.description}", modifier = Modifier.padding(end = 16.dp))
                    Text(text = "Account: ${transaction.accountId}", modifier = Modifier.padding(end = 16.dp))
                }
                HorizontalDivider()
            }
        }
    }

    @Composable
    fun TransactionPage() {
        // TODO: Not load all data, just load the first 10 transactions. load more when scrolling
        val transactions by transactionFetchViewModel.transactions.observeAsState(initial = emptyList())

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