package com.example.nanopost.domain.repository

interface SettingsRepository {
    suspend fun setRefreshToken(token: String)

    suspend fun setAccessToken(token: String)

    suspend fun setUserId(id: String)

    suspend fun setUsername(name: String)

    suspend fun setPassword(password: String)
}