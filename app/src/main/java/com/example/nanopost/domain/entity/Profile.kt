package com.example.nanopost.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Profile (
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