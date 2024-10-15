package com.edogawakazuki.bookkeeping

import FilterPopUp
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.edogawakazuki.bookkeeping.data.repository.AppDatabaseProvider
import com.edogawakazuki.bookkeeping.data.repository.TransactionRepository
import com.edogawakazuki.bookkeeping.data.viewmodel.TransactionFetchViewModel
import com.edogawakazuki.bookkeeping.data.viewmodelFactory.TransactionFetchViewModelFactory
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme

private lateinit var transactionFetchViewModel: TransactionFetchViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val openFilterPopUp = mutableStateOf(false)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                                    Button(onClick = {
                                        openFilterPopUp.value = true
                                    }) {
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
                    content = {
                        FilterPopUp(
                            this@MainActivity,
                            openFilterPopUp,
                            transactionFetchViewModel,
                        )
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
            val toastMsg: String = when (val action: String? = data?.getStringExtra("action")) {
                "insert" -> {
                    "Transaction added"
                }
                "update" -> {
                    "Transaction updated"
                }
                "delete" -> {
                    "Transaction deleted"
                }
                else -> {
                    "Unknown action $action"
                }
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
                    1 -> TransactionPage(
                        this@MainActivity,
                        transactionFetchViewModel,
                        onItemClick = { intent ->
                            editTransactionLauncher.launch(intent)
                        }
                    )
                    2 -> SettingsContent()
                }
            }
        }
    }

    @Composable
    fun HomePage() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Home Screen")
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


}