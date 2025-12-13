package com.example.shared.network.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageModel(
    val id: String,
    val owner: ProfileCompactModel,
    val dateCreated: Long,
    val sizes: List<ImageSizeModel>,
)