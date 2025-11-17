package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.UsernameCheckResult
import com.example.nanopost.domain.repository.AuthRepository
import javax.inject.Inject

class CheckUsernameUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String): UsernameCheckResult {
        return authRepository.checkUsername(username)
    }
}