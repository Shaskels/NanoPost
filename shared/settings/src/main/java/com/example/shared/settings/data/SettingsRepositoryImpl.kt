package com.example.shared.settings.data

import com.example.shared.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsDataStore: SettingsDataStore) :
    SettingsRepository {

    override suspend fun setRefreshToken(token: String) {
        return settingsDataStore.setRefreshToken(token)
    }

    override suspend fun setAccessToken(token: String) {
        return settingsDataStore.setAccessToken(token)
    }

    override suspend fun setUserId(id: String) {
        return settingsDataStore.setUserId(id)
    }

    override suspend fun getUserId(): String {
        return settingsDataStore.getUserId() ?: ""
    }

    override suspend fun setUsername(name: String) {
        return settingsDataStore.setUsername(name)
    }

    override suspend fun setPassword(password: String) {
        return settingsDataStore.setPassword(password)
    }

    override suspend fun isUserLogin(): Boolean {
        return settingsDataStore.isUserLogin()
    }

    override suspend fun clearData() {
        settingsDataStore.clearData()
    }

}