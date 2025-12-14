package com.example.feature.feed.presentation.feedScreen.screenState

data class FeedScreenState(
    val likedPosts: List<String>,
    val unlikedPosts: List<String>,
    val likeError: LikeErrors,
)