package com.example.nanopost.data.remote.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel (
    val id: String,
    val username: String,
    val displayName: String?,
    val bio: String?,
    val avatarId: String?,
    val avatarSmall: String?,
    val avatarLarge: String?,
    val subscribed: Boolean,
    val subscribersCount: Int,
    val postsCount: Int,
    val imagesCount: Int,
)