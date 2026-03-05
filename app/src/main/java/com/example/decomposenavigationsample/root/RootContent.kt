package com.example.decomposenavigationsample.root

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.decomposenavigationsample.NavAction
import com.example.decomposenavigationsample.R
import com.example.decomposenavigationsample.drawer.DrawerNavComponent
import com.example.decomposenavigationsample.drawer.DrawerNavContent
import com.example.decomposenavigationsample.transaction.TransactionComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootContent(component: RootComponent) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerStack by component.drawerNav.stack.subscribeAsState()

    val activeNavAction by when (val drawerActive = drawerStack.active.instance) {
        is DrawerNavComponent.Child.Transaction -> {
            val transactionStack by drawerActive.component.stack.subscribeAsState()
            when (val transactionActive = transactionStack.active.instance) {
                is TransactionComponent.Child.Processing -> transactionActive.component.navAction
                else -> drawerActive.component.navAction
            }
        }
        is DrawerNavComponent.Child.History -> drawerActive.component.navAction
    }.subscribeAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    onTransactionClick = {
                        component.drawerNav.switchToTransaction()
                        scope.launch { drawerState.close() }
                    },
                    onHistoryClick = {
                        component.drawerNav.switchToHistory()
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(Color.Blue),
                    title = { Text("App") },
                    navigationIcon = {
                        when (activeNavAction) {
                            NavAction.ShowMenu -> {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Image(
                                        painterResource(R.drawable.menu),
                                        contentDescription = "Menu",
                                        colorFilter = ColorFilter.tint(Color.Blue)
                                    )
                                }
                            }
                            NavAction.ShowBack -> {
                                IconButton(onClick = { component.onBackClick() }) {
                                    Image(
                                        painterResource(R.drawable.arrow_back),
                                        contentDescription = "Back",
                                        colorFilter = ColorFilter.tint(Color.Blue)
                                    )
                                }
                            }
                            NavAction.Hidden -> {
                                // dont show any icon
                                /* no-op */
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                DrawerNavContent(component = component.drawerNav)
            }
        }
    }
}

@Composable
private fun DrawerContent(
    onTransactionClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Column {
        Text(
            text = "Navigation",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        NavigationDrawerItem(
            label = { Text("Transactions") },
            selected = false,
            onClick = onTransactionClick
        )
        NavigationDrawerItem(
            label = { Text("History") },
            selected = false,
            onClick = onHistoryClick
        )
    }
}