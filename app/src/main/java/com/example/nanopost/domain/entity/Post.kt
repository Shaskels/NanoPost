package com.example.nanopost.domain.entity

data class Post(
    val id: String,
    val owner: ProfileCompact,
    val dateCreated: Long,
    val text: String?,
    val images: List<Image>,
    val likes: Likes,
)