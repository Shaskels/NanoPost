package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.repository.AuthRepository
import com.example.nanopost.domain.repository.SettingsRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String) {
        val result = authRepository.loginUser(username, password)
        settingsRepository.setUserId(result.userId)
        settingsRepository.setAccessToken(result.token)
        settingsRepository.setUsername(username)
        settingsRepository.setPassword(password)
    }
}