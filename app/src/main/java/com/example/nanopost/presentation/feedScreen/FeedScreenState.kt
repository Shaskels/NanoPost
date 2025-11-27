package com.example.nanopost.presentation.feedScreen

import com.example.nanopost.domain.entity.Post

sealed interface FeedScreenState {
    data object Initial: FeedScreenState
    data object Loading: FeedScreenState
    data object Error: FeedScreenState
    data class Content(val posts: List<Post>): FeedScreenState
}