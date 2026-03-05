package com.example.decomposenavigationsample.transaction.processing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation

@Composable
fun ProcessingContent(
    component: ProcessingComponent
) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is ProcessingComponent.Child.NoteToMerchant -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Note To Merchant")
                        Button(
                            onClick = { component.navigateToInputCardNo() }
                        ) {
                            Text(text = "Next")
                        }
                    }
                }
            }
            is ProcessingComponent.Child.InputCardNo -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Input Card No")
                        Button(
                            onClick = { component.navigateToInputExpiryDate() }
                        ) {
                            Text(text = "Next")
                        }
                    }
                }
            }
            is ProcessingComponent.Child.InputExpiryDate -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Input Expiry Date")
                        Button(
                            onClick = { component.navigateToConfirmation() }
                        ) {
                            Text(text = "Next")
                        }
                    }
                }
            }
            is ProcessingComponent.Child.Confirmation -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Confirmation")
                        Button(
                            onClick = { component.navigateWhenComplete() }
                        ) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}