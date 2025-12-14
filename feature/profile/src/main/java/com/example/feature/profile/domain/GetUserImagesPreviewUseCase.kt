package com.example.feature.profile.domain

import com.example.shared.domain.entity.Image
import com.example.shared.domain.repository.ImagesRepository
import com.example.shared.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserImagesPreviewUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(profileId: String?): List<Image>{
        return withContext(Dispatchers.IO) {
            val userId = profileId ?: settingsRepository.getUserId()
            imagesRepository.getProfileImagesPreview(userId)
        }
    }
}