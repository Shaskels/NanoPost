package com.example.nanopost.presentation.newPostScreen.newPostScreenState

data class NewPostScreenState(
    val postText: String,
    val postImages: List<String>,
    val uploadState: UploadState
)