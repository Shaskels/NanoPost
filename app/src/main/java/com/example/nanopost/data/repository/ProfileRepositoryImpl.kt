package com.example.nanopost.data.repository

import com.example.nanopost.data.remote.ApiService
import com.example.nanopost.data.remote.mappers.toDomainProfile
import com.example.nanopost.domain.entity.Profile
import com.example.nanopost.domain.repository.ProfileRepository
import jakarta.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {
    override suspend fun getProfile(profileId: String): Profile {
        return apiService.getProfile(profileId).toDomainProfile()
    }
}