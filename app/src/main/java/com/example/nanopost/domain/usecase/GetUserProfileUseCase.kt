package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.Profile
import com.example.nanopost.domain.repository.ProfileRepository
import com.example.nanopost.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profileId: String?): Profile {
        return withContext(Dispatchers.IO) {
            val userId = profileId ?: settingsRepository.getUserId()
            profileRepository.getProfile(userId)
        }
    }
}