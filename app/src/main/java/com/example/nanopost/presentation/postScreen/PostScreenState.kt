package com.example.nanopost.presentation.postScreen

import com.example.nanopost.domain.entity.Post

sealed interface PostScreenState {

    data object Loading: PostScreenState
    data object Error: PostScreenState
    data class Content(val post: Post): PostScreenState
}