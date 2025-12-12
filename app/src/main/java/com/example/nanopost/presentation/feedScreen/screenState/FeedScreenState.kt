package com.example.nanopost.presentation.feedScreen.screenState

data class FeedScreenState(
    val likedPosts: List<String>,
    val unlikedPosts: List<String>,
    val likeError: LikeErrors,
)