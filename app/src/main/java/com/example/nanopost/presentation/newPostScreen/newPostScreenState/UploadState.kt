package com.example.nanopost.presentation.newPostScreen.newPostScreenState

sealed interface UploadState {
    data object None : UploadState
    data object Success : UploadState
    data object Failure : UploadState
}