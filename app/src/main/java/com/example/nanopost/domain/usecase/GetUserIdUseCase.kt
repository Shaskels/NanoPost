package com.example.nanopost.domain.usecase

import com.example.shared.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(): String {
        return settingsRepository.getUserId()
    }
}