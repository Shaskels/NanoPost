package com.example.nanopost.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsernameCheckResponse(val result: UsernameCheckResultModel)

@Serializable
enum class UsernameCheckResultModel {
    @SerialName("TooShort") TooShort,
    @SerialName("TooLong") TooLong,
    @SerialName("InvalidCharacter") InvalidCharacter,
    @SerialName("Taken") Taken,
    @SerialName("Free") Free,
}

