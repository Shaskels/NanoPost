package com.example.feature.newpost.presentation.newPostScreen.newPostScreenState

import android.net.Uri

data class NewPostScreenState(
    val postText: String,
    val postImages: List<Uri>,
    val uploadState: UploadState
)