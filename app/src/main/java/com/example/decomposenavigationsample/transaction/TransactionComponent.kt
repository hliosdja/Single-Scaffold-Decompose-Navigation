package com.example.decomposenavigationsample.transaction

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.example.decomposenavigationsample.NavAction
import com.example.decomposenavigationsample.transaction.processing.DefaultProcessingComponent
import com.example.decomposenavigationsample.transaction.processing.ProcessingComponent
import kotlinx.serialization.Serializable

interface TransactionComponent {
    val stack: Value<ChildStack<*, Child>>
    val navAction: Value<NavAction>

    fun navigateToInput()
    fun navigateToTransactionType()
    fun navigateToProcessing()
    fun navigateToResult()
    fun onBackClicked()

    sealed interface Child {
        data class Input(val component: InputComponent) : Child
        data class TransactionType(val component: TransactionTypeComponent) : Child
        data class Processing(val component: ProcessingComponent) : Child
        data class Result(val component: ResultComponent) : Child
    }
}

class DefaultTransactionComponent(
    componentContext: ComponentContext
): TransactionComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, TransactionComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.Input,
            handleBackButton = false,
            childFactory = ::createChild
        )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): TransactionComponent.Child {
        return when (config) {
            is Config.Input -> TransactionComponent.Child.Input(
                component = InputComponent(componentContext)
            )
            is Config.TransactionType -> TransactionComponent.Child.TransactionType(
                component = TransactionTypeComponent(componentContext)
            )
            is Config.Processing -> TransactionComponent.Child.Processing(
                component = DefaultProcessingComponent(
                    componentContext = componentContext,
                    onFlowComplete = { navigateToResult() }
                )
            )
            is Config.Result -> TransactionComponent.Child.Result(
                component = ResultComponent(componentContext)
            )
        }
    }

    override val navAction: Value<NavAction> = stack.map { childStack ->
        when (val instance = childStack.active.instance) {
            is TransactionComponent.Child.Input -> NavAction.ShowMenu
            is TransactionComponent.Child.TransactionType -> NavAction.ShowBack
            is TransactionComponent.Child.Processing -> instance.component.navAction.value
            is TransactionComponent.Child.Result -> NavAction.ShowBack
        }
    }

    override fun onBackClicked() {
        when (val active = stack.value.active.instance) {
            is TransactionComponent.Child.Processing -> {
                val isBackHandledInternally = active.component.onBackClicked()
                if (!isBackHandledInternally) {
                    navigation.pop()
                }
            }
            else -> navigation.pop()
        }
    }

    override fun navigateToInput() {
        navigation.replaceAll(Config.Input)
    }

    override fun navigateToTransactionType() {
        navigation.pushNew(Config.TransactionType)
    }

    override fun navigateToProcessing() {
        navigation.pushNew(Config.Processing)
    }

    override fun navigateToResult() {
        navigation.pushNew(Config.Result)
    }

    @Serializable
    sealed class Config {

        @Serializable
        data object Input : Config()

        @Serializable
        data object TransactionType : Config()

        @Serializable
        data object Processing : Config()

        @Serializable
        data object Result : Config()
    }
}

class InputComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext

class TransactionTypeComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext

class ResultComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext