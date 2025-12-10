package com.example.nanopost.data.remote.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LikesModel(
    val liked: Boolean,
    val likesCount: Int,
)