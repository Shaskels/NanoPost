package com.example.nanopost.domain.entity

import com.example.nanopost.data.remote.model.ImageModel
import com.example.nanopost.data.remote.model.LikesModel
import com.example.nanopost.data.remote.model.ProfileCompactModel

data class Post(
    val id: String,
    val owner: ProfileCompact,
    val dataCreated: Long,
    val text: String?,
    val images: List<Image>,
    val likes: Likes,
)