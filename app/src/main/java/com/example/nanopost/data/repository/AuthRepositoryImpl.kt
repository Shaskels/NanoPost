package com.example.nanopost.data.repository

import com.example.nanopost.data.remote.AuthService
import com.example.nanopost.data.remote.mappers.toDomainEntity
import com.example.nanopost.data.remote.model.RegisterRequest
import com.example.nanopost.domain.entity.AuthResult
import com.example.nanopost.domain.entity.UsernameCheckResult
import com.example.nanopost.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun checkUsername(username: String): UsernameCheckResult {
        return authService.checkUsername(username).usernameCheckResultModel.toDomainEntity()
    }

    override suspend fun loginUser(username: String, password: String): AuthResult {
        return authService.loginUser(username, password).toDomainEntity()
    }

    override suspend fun registerUser(username: String, password: String): AuthResult {
        return authService.registerUser(RegisterRequest(username, password)).toDomainEntity()
    }
}