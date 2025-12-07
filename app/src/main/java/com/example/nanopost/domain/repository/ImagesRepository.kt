package com.example.nanopost.domain.repository

import com.example.nanopost.domain.entity.Image

interface ImagesRepository {
    suspend fun getProfileImages(profileId: String): List<Image>
}