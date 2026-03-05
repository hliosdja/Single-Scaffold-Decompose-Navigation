package com.example.decomposenavigationsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.decomposenavigationsample.root.DefaultRootComponent
import com.example.decomposenavigationsample.root.RootContent
import com.example.decomposenavigationsample.ui.theme.DecomposeNavigationSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val root = DefaultRootComponent(componentContext = defaultComponentContext())

        setContent {
            DecomposeNavigationSampleTheme {
                RootContent(root)
            }
        }
    }
}