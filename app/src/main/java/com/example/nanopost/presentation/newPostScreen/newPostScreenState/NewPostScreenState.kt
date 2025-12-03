package com.example.nanopost.presentation.newPostScreen.newPostScreenState

import android.net.Uri

data class NewPostScreenState(
    val postText: String,
    val postImages: List<Uri>,
    val uploadState: UploadState
)