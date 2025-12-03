package com.example.nanopost.presentation.mainScreen

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.nanopost.presentation.theme.NanoPostTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashscreen.setKeepOnScreenCondition {
            viewModel.isUserAuth.value == null
        }

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.Companion.dark(Color.TRANSPARENT),
            statusBarStyle = SystemBarStyle.Companion.dark(Color.TRANSPARENT)
        )
        setContent {
            NanoPostTheme {
                MainScreen(mainViewModel = viewModel)
            }
        }
    }
}