package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.repository.ImagesRepository
import com.example.nanopost.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): List<Image>{
        return withContext(Dispatchers.IO) {
            val userId = settingsRepository.getUserId()
            imagesRepository.getProfileImages(userId)
        }
    }
}