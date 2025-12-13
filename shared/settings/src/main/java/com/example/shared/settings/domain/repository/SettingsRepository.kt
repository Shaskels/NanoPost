package com.example.shared.settings.domain.repository

interface SettingsRepository {
    suspend fun setRefreshToken(token: String)

    suspend fun setAccessToken(token: String)

    suspend fun setUserId(id: String)

    suspend fun getUserId(): String

    suspend fun setUsername(name: String)

    suspend fun setPassword(password: String)

    suspend fun isUserLogin(): Boolean

    suspend fun clearData()
}