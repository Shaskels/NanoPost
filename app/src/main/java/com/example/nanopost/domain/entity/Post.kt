package com.example.nanopost.domain.entity

import com.example.shared.domain.entity.ProfileCompact

data class Post(
    val id: String,
    val owner: ProfileCompact,
    val dateCreated: Long,
    val text: String?,
    val images: List<Image>,
    val likes: Likes,
)