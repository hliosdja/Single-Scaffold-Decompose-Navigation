package com.example.decomposenavigationsample.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.decomposenavigationsample.NavAction
import com.example.decomposenavigationsample.drawer.DefaultDrawerNavComponent
import com.example.decomposenavigationsample.drawer.DrawerNavComponent
import kotlinx.serialization.Serializable

interface RootComponent {
    val drawerNav: DrawerNavComponent
    val navAction: Value<NavAction>

    fun onBackClick()
}

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    override val drawerNav: DrawerNavComponent = DefaultDrawerNavComponent(
        componentContext = childContext("drawer_nav")
    )

    override val navAction: Value<NavAction> = drawerNav.navAction

    override fun onBackClick() {
        drawerNav.onBackClick()
    }
}