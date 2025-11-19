package com.example.nanopost.domain.repository

import com.example.nanopost.domain.entity.AuthResult
import com.example.nanopost.domain.entity.UsernameCheckResult

interface AuthRepository {

    suspend fun checkUsername(username: String): UsernameCheckResult

    suspend fun loginUser(username: String, password: String): AuthResult

    suspend fun registerUser(username: String, password: String): AuthResult
}