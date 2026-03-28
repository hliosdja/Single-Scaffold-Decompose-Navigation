package com.example.decomposenavigationsample.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.decomposenavigationsample.transaction.processing.ProcessingContent

@Composable
fun TransactionContent(component: TransactionComponent) {
    val stack by component.stack.subscribeAsState()

    Children(
        stack = component.stack,
        animation = stackAnimation(fade()),
    ) {
        when (val instance = it.instance) {
            is TransactionComponent.Child.Input -> {
                var counter by rememberSaveable { mutableIntStateOf(0) }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Input")
                        Text(text = "Counter: $counter")
                        Button(
                            onClick = { counter++ }
                        ) {
                            Text(text = "Increment counter")
                        }
                        Button(
                            onClick = { component.navigateToTransactionType() }
                        ) {
                            Text(text = "Next")
                        }
                    }
                }
            }
            is TransactionComponent.Child.TransactionType -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Transaction Type")
                        Button(
                            onClick = { component.navigateToProcessing() }
                        ) {
                            Text(text = "Next")
                        }
                    }
                }
            }
            is TransactionComponent.Child.Processing -> {
                ProcessingContent(instance.component)
            }
            is TransactionComponent.Child.Result -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Result")
                        Button(
                            onClick = { component.navigateToInput() }
                        ) {
                            Text(text = "Back to Input")
                        }
                    }
                }
            }
        }
    }
}