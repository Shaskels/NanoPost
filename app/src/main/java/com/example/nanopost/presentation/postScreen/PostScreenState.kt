package com.example.nanopost.presentation.postScreen

import com.example.shared.domain.entity.Post
import com.example.shared.network.domain.exceptions.AppException

sealed interface PostScreenState {

    data object Loading: PostScreenState
    data class Error(val e: AppException): PostScreenState
    data class Content(val post: Post): PostScreenState
}