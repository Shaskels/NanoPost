package com.example.nanopost.presentation

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nanopost.presentation.mainScreen.MainScreen
import com.example.nanopost.presentation.theme.NanoPostTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(TRANSPARENT),
            statusBarStyle = SystemBarStyle.dark(TRANSPARENT)
        )
        setContent {
            NanoPostTheme {
                MainScreen()
            }
        }
    }
}