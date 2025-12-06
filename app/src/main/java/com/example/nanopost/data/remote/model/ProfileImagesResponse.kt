package com.example.nanopost.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImagesResponse(
    val count: Int,
    val total: Int,
    val offset: String?,
    val items: List<ImageModel>
)