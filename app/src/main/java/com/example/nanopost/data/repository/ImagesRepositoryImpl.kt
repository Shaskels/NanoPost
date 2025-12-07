package com.example.nanopost.data.repository

import com.example.nanopost.data.remote.ApiService
import com.example.nanopost.data.remote.mappers.toDomainImage
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.repository.ImagesRepository
import jakarta.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ImagesRepository {
    override suspend fun getProfileImages(profileId: String): List<Image> {
        return apiService.getProfileImages(profileId).items.map { it.toDomainImage() }
    }
}