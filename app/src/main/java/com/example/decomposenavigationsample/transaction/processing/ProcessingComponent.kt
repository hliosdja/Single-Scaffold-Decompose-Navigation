package com.example.decomposenavigationsample.transaction.processing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.example.decomposenavigationsample.NavAction
import com.example.decomposenavigationsample.transaction.processing.confirmation.ConfirmationComponent
import com.example.decomposenavigationsample.transaction.processing.confirmation.DefaultConfirmationComponent
import kotlinx.serialization.Serializable

interface ProcessingComponent {
    val stack: Value<ChildStack<*, Child>>
    val navAction: Value<NavAction>

    fun onBackClicked(): Boolean

    fun navigateToInputCardNo()
    fun navigateToInputExpiryDate()
    fun navigateToConfirmation()
    fun navigateWhenComplete()

    sealed interface Child {
        data class NoteToMerchant(val component: NoteComponent): Child
        data class InputCardNo(val component: InputCardNoComponent): Child
        data class InputExpiryDate(val component: InputExpiryDateComponent): Child
        data class Confirmation(val component: ConfirmationComponent): Child
    }
}

class DefaultProcessingComponent(
    componentContext: ComponentContext,
    private val onFlowComplete: () -> Unit
): ProcessingComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, ProcessingComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.NoteToMerchant,
            handleBackButton = false,
            childFactory = ::createChildren
        )

    private fun createChildren(
        config: Config,
        componentContext: ComponentContext
    ): ProcessingComponent.Child {
        return when (config) {
            is Config.NoteToMerchant -> ProcessingComponent.Child.NoteToMerchant(
                component = NoteComponent(componentContext)
            )
            is Config.InputCardNo -> ProcessingComponent.Child.InputCardNo(
                component = InputCardNoComponent(componentContext)
            )
            is Config.InputExpiryDate -> ProcessingComponent.Child.InputExpiryDate(
                component = InputExpiryDateComponent(componentContext)
            )
            is Config.Confirmation -> ProcessingComponent.Child.Confirmation(
                component = DefaultConfirmationComponent(
                    componentContext = componentContext,
                    onDone = onFlowComplete
                )
            )
        }
    }

    override val navAction: Value<NavAction> = stack.map { childStack ->
        when (val active = childStack.active.instance) {
            is ProcessingComponent.Child.NoteToMerchant -> NavAction.Hidden
            is ProcessingComponent.Child.InputCardNo -> NavAction.ShowBack
            is ProcessingComponent.Child.InputExpiryDate -> NavAction.ShowBack
            is ProcessingComponent.Child.Confirmation -> NavAction.ShowBack
        }
    }

    override fun onBackClicked(): Boolean {
        return if (stack.value.backStack.isNotEmpty()) {
            navigation.pop()
            true
        } else {
            false
        }
    }

    override fun navigateToInputCardNo() {
        navigation.pushNew(Config.InputCardNo)
    }

    override fun navigateToInputExpiryDate() {
        navigation.pushNew(Config.InputExpiryDate)
    }

    override fun navigateToConfirmation() {
        navigation.pushNew(Config.Confirmation)
    }

    override fun navigateWhenComplete() {
        onFlowComplete()
    }

    @Serializable
    sealed class Config {
        @Serializable object NoteToMerchant: Config()
        @Serializable object InputCardNo: Config()
        @Serializable object InputExpiryDate: Config()
        @Serializable object Confirmation: Config()
    }
}

class NoteComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext

class InputCardNoComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext

class InputExpiryDateComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext