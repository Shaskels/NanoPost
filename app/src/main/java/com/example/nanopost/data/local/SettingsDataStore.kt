package com.example.nanopost.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val refreshToken = stringPreferencesKey("refreshToken")
    private val accessToken = stringPreferencesKey("accessToken")
    private val userId = stringPreferencesKey("userId")
    private val username = stringPreferencesKey("username")
    private val passwordKey = stringPreferencesKey("password")

    suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences -> preferences[refreshToken] }.first()
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.map { preferences -> preferences[accessToken] }.first()
    }

    suspend fun getUserId(): String? {
        return dataStore.data.map { preferences -> preferences[userId] }.first()
    }

    suspend fun getUsername(): String? {
        return dataStore.data.map { preferences -> preferences[username] }.first()
    }

    suspend fun getPassword(): String? {
        return dataStore.data.map { preferences -> preferences[passwordKey] }.first()
    }

    suspend fun setRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[refreshToken] = token
        }
    }

    suspend fun setAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[accessToken] = token
        }
    }

    suspend fun setUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[userId] = id
        }
    }

    suspend fun setUsername(name: String) {
        dataStore.edit {preferences ->
            preferences[username] = name
        }
    }

    suspend fun setPassword(password: String) {
        dataStore.edit {preferences ->
            preferences[passwordKey] = password
        }
    }
}