package com.example.shared.network.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PostModel(
    val id: String,
    val owner: ProfileCompactModel,
    val dateCreated: Long,
    val text: String?,
    val images: List<ImageModel>,
    val likes: LikesModel,
)