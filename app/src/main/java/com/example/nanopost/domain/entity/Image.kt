package com.example.nanopost.domain.entity

import com.example.shared.domain.entity.ProfileCompact

data class Image(
    val id: String,
    val owner: ProfileCompact,
    val dateCreated: Long,
    val sizes: List<ImageSize>,
)