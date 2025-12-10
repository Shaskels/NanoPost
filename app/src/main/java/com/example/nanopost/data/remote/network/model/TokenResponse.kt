package com.example.nanopost.data.remote.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("token")
    val token: String,
    @SerialName("userId")
    val userId: String
)