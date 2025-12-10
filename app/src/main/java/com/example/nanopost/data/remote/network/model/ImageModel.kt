package com.example.nanopost.data.remote.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageModel(
    val id: String,
    val owner: ProfileCompactModel,
    val dateCreated: Long,
    val sizes: List<ImageSizeModel>,
)