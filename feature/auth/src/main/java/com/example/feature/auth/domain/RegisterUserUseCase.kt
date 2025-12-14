package com.example.feature.auth.domain

import com.example.shared.domain.repository.AuthRepository
import com.example.shared.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String) {
        val result = authRepository.registerUser(username, password)
        settingsRepository.setUserId(result.userId)
        settingsRepository.setAccessToken(result.token)
        settingsRepository.setUsername(username)
        settingsRepository.setPassword(password)
    }
}