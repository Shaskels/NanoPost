package com.example.nanopost.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageSizeModel(
    val width: Int,
    val height: Int,
    val url: String,
)