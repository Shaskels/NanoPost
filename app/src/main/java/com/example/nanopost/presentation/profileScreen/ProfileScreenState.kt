package com.example.nanopost.presentation.profileScreen

import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.entity.Profile
import com.example.nanopost.presentation.feedScreen.LikeErrors

sealed interface ProfileScreenState {

    data object Loading: ProfileScreenState

    data class Error(val errorType: ErrorType): ProfileScreenState

    data class Content(val profile: Profile, val images: List<Image>, val likedPosts: List<String>, val unlikedPosts: List<String>): ProfileScreenState
}

sealed interface ErrorType {
    data object NetworkError: ErrorType
    data object AuthenticationError: ErrorType
    data object UnknownError: ErrorType
}