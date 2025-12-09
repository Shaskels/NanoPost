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
    data class Profile(val profileId: String?): Route

    @Serializable
    data object Empty: Route

    @Serializable
    data object NewPost: Route

    @Serializable
    data class ProfilePosts(val profileId: String?): Route
}