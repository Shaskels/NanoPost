package com.example.shared.network.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T> (
    val count: Int,
    val total: Int,
    val offset: String?,
    val items: List<T>
)