package com.example.decomposenavigationsample

sealed class NavAction {
    object ShowMenu : NavAction()
    object ShowBack : NavAction()
    object Hidden   : NavAction()
}
