package com.example.feature.feed.presentation.feedScreen.screenState

sealed interface LikeErrors {
    data object NoError: LikeErrors
    data object NetworkError: LikeErrors
    data object AuthenticationError: LikeErrors
    data object UnknownError: LikeErrors
}