package com.example.nanopost.domain.entity

data class Image(
    val id: String,
    val owner: ProfileCompact,
    val dateCreated: Long,
    val sizes: List<ImageSize>,
)