package com.example.shared.network.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LikesModel(
    val liked: Boolean,
    val likesCount: Int,
)