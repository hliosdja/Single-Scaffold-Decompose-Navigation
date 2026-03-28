package com.example.decomposenavigationsample.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.example.decomposenavigationsample.drawer.DefaultDrawerNavComponent
import com.example.decomposenavigationsample.drawer.DrawerNavComponent

interface RootComponent {
    val drawerNav: DrawerNavComponent

    fun onBackClick()
}

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    override val drawerNav: DrawerNavComponent = DefaultDrawerNavComponent(
        componentContext = childContext("drawer_nav")
    )

    override fun onBackClick() {
        drawerNav.onBackClick()
    }
}