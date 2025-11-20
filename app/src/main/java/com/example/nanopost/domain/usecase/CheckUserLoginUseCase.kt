package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.repository.SettingsRepository
import javax.inject.Inject

class CheckUserLoginUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(): Boolean {
        return settingsRepository.isUserLogin()
    }
}