package com.example.nanopost.presentation.mainScreen

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Auth: Route

    @Serializable
    data object Feed: Route

    @Serializable
    data object Profile: Route

    @Serializable
    data object SplashScreen: Route

    @Serializable
    data object NewPost: Route
}