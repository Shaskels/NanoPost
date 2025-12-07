package com.example.nanopost.domain.repository

import com.example.nanopost.domain.entity.Profile

interface ProfileRepository {

    suspend fun getProfile(profileId: String): Profile
}