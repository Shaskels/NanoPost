package com.example.shared.network.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageSizeModel(
    val width: Int,
    val height: Int,
    val url: String,
)