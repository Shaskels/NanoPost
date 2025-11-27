package com.example.nanopost.domain.entity

import com.example.nanopost.data.remote.model.ImageSizeModel
import com.example.nanopost.data.remote.model.ProfileCompactModel

data class Image(
    val id: String,
    val owner: ProfileCompact,
    val dateCreated: Long,
    val sizes: List<ImageSize>,
)