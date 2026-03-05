package com.example.decomposenavigationsample.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.example.decomposenavigationsample.NavAction
import kotlinx.serialization.Serializable

interface HistoryComponent {
    val stack: Value<ChildStack<*, Child>>
    val navAction: Value<NavAction>

    fun navigateToDetails()
    fun navigateBack()
    fun onBackClicked()

    sealed interface Child {
        data class List(val component: ListComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }
}

class DefaultHistoryComponent(
    componentContext: ComponentContext
): HistoryComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, HistoryComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.List,
            handleBackButton = false,
            childFactory = ::createChild
        )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): HistoryComponent.Child {
        return when (config) {
            is Config.List -> HistoryComponent.Child.List(
                component = ListComponent(componentContext)
            )
            is Config.Details -> HistoryComponent.Child.Details(
                component = DetailsComponent(componentContext)
            )
        }
    }

    override val navAction: Value<NavAction> = stack.map { childStack ->
        when (val instance = childStack.active.instance) {
            is HistoryComponent.Child.List -> NavAction.ShowMenu
            is HistoryComponent.Child.Details -> NavAction.ShowBack
        }
    }

    override fun onBackClicked() {
        navigation.pop()
    }

    override fun navigateToDetails() {
        navigation.pushNew(Config.Details)
    }

    override fun navigateBack() {
        navigation.pop()
    }

    @Serializable
    sealed class Config {

        @Serializable
        data object List : Config()

        @Serializable
        data object Details : Config()
    }
}

class ListComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext

class DetailsComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext