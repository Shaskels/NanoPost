package com.example.feature.profile.presentation.profileScreen

import com.example.shared.domain.entity.Image
import com.example.shared.domain.entity.Profile

sealed interface ProfileScreenState {

    data object Loading : ProfileScreenState

    data class Error(val errorType: ErrorType) : ProfileScreenState

    data class Content(
        val profile: Profile,
        val images: List<Image>,
        val likedPosts: List<String>,
        val unlikedPosts: List<String>,
        val subscribed: Boolean?,
    ) : ProfileScreenState
}

sealed interface ErrorType {
    data object NetworkError : ErrorType
    data object AuthenticationError : ErrorType
    data object UnknownError : ErrorType
}