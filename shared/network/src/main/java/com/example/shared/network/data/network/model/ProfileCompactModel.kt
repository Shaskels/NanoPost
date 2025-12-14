package com.example.shared.network.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileCompactModel(
    val id: String,
    val username: String,
    val displayName: String?,
    val avatarUrl: String?,
    val subscribed: Boolean?,
)