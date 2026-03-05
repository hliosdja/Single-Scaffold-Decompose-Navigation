package com.example.decomposenavigationsample.drawer

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.decomposenavigationsample.history.HistoryContent
import com.example.decomposenavigationsample.transaction.TransactionContent

@Composable
fun DrawerNavContent(component: DrawerNavComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is DrawerNavComponent.Child.Transaction -> TransactionContent(child.component)
            is DrawerNavComponent.Child.History -> HistoryContent(child.component)
        }
    }
}