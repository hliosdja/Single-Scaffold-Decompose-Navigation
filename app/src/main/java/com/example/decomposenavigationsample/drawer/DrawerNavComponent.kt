package com.example.decomposenavigationsample.drawer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.example.decomposenavigationsample.NavAction
import com.example.decomposenavigationsample.history.DefaultHistoryComponent
import com.example.decomposenavigationsample.history.HistoryComponent
import com.example.decomposenavigationsample.transaction.DefaultTransactionComponent
import com.example.decomposenavigationsample.transaction.TransactionComponent
import kotlinx.serialization.Serializable

interface DrawerNavComponent {
    val stack: Value<ChildStack<*, Child>>
    val navAction: Value<NavAction>

    fun switchToTransaction()
    fun switchToHistory()
    fun onBackClick()

    sealed class Child {
        class Transaction(val component: TransactionComponent) : Child()
        class History(val component: HistoryComponent) : Child()
    }
}

class DefaultDrawerNavComponent(
    componentContext: ComponentContext,
) : DrawerNavComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, DrawerNavComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.Transaction,
            handleBackButton = false,
            childFactory = ::createChild
        )

    override val navAction: Value<NavAction> = stack.map { childStack ->
        when (val instance = childStack.active.instance) {
            is DrawerNavComponent.Child.Transaction -> instance.component.navAction.value
            is DrawerNavComponent.Child.History -> instance.component.navAction.value
        }
    }

    override fun switchToTransaction() {
        navigation.replaceCurrent(Config.Transaction)
    }

    override fun switchToHistory() {
        navigation.replaceCurrent(Config.History)
    }

    override fun onBackClick() {
        when (val instance = stack.value.active.instance) {
            is DrawerNavComponent.Child.Transaction -> instance.component.onBackClicked()
            is DrawerNavComponent.Child.History -> instance.component.onBackClicked()
        }
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): DrawerNavComponent.Child {
        return when (config) {
            is Config.Transaction -> DrawerNavComponent.Child.Transaction(
                component = DefaultTransactionComponent(componentContext)
            )
            is Config.History -> DrawerNavComponent.Child.History(
                component = DefaultHistoryComponent(componentContext)
            )
        }
    }

    @Serializable
    sealed class Config {
        data object Transaction : Config()
        data object History : Config()
    }
}