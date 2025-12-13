package com.example.feature.auth.domain

import com.example.shared.domain.entity.UsernameCheckResult
import com.example.shared.domain.repository.AuthRepository
import javax.inject.Inject

class CheckUsernameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String): UsernameCheckResult {
        return authRepository.checkUsername(username)
    }
}