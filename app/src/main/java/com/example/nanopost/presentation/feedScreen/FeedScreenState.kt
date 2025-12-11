package com.example.nanopost.presentation.feedScreen

data class FeedScreenState(
    val likedPosts: List<String>,
    val unlikedPosts: List<String>,
    val likeError: LikeErrors
)

sealed interface LikeErrors {
    data object NoError: LikeErrors
    data object NetworkError: LikeErrors
    data object AuthenticationError: LikeErrors
    data object UnknownError: LikeErrors
}