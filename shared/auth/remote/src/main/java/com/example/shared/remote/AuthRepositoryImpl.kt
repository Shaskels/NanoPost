package com.example.shared.remote

import com.example.shared.domain.entity.AuthResult
import com.example.shared.domain.entity.UsernameCheckResult
import com.example.shared.domain.repository.AuthRepository
import com.example.shared.network.data.network.AuthService
import com.example.shared.network.data.network.model.RegisterRequest
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun checkUsername(username: String): UsernameCheckResult {
        return authService.checkUsername(username).result.toDomainEntity()
    }

    override suspend fun loginUser(username: String, password: String): AuthResult {
        return authService.loginUser(username, password).toDomainEntity()
    }

    override suspend fun registerUser(username: String, password: String): AuthResult {
        return authService.registerUser(RegisterRequest(username, password)).toDomainEntity()
    }
}