package com.example.decomposenavigationsample.transaction.processing.confirmation

import com.arkivanov.decompose.ComponentContext

interface ConfirmationComponent {
    fun onFlowComplete()
}

class DefaultConfirmationComponent(
    componentContext: ComponentContext,
    private val onDone: () -> Unit
): ConfirmationComponent, ComponentContext by componentContext {
    override fun onFlowComplete() {
        onDone()
    }
}