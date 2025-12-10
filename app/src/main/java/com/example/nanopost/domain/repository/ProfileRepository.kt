package com.example.nanopost.domain.repository

import androidx.paging.PagingData
import com.example.nanopost.domain.entity.Profile
import com.example.nanopost.domain.entity.ProfileCompact
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(profileId: String): Profile

    fun getProfileSubscribers(profileId: String): Flow<PagingData<ProfileCompact>>
}