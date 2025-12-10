package com.example.nanopost.data.remote.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T> (
    val count: Int,
    val total: Int,
    val offset: String?,
    val items: List<T>
)