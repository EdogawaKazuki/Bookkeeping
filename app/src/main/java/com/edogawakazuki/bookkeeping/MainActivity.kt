package com.edogawakazuki.bookkeeping

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Scroller
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.edogawakazuki.bookkeeping.ui.theme.BookkeepingTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

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
                                    Button(onClick = { /* Handle refresh! */ }) {
                                        Text("Try me")
                                    }
                                }
                            )

                            TabBar()
                        }
                    },
                    floatingActionButton = {
                        Column {
                            FloatingActionButton (onClick = { CreateTransication() } ) {
                                Text("+")
                            }
                            FloatingActionButton (onClick = { CreateTransication() } ) {
                                Text("+")
                            }

                        }
                    },
                    content = { innerPadding ->
                        Box(
                            modifier = Modifier.padding(innerPadding)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
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

    private fun CreateTransication() {
        Toast.makeText(this, "Create Transaction", Toast.LENGTH_SHORT).show()
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
                    0 -> HomeContent()
                    1 -> ProfileContent()
                    2 -> SettingsContent()
                }
            }
        }
    }

    @Composable
    fun HomeContent() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Home Screen")
        }
    }

    @Composable
    fun ProfileContent() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile Screen")
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