package com.example.shared.domain.repository

import androidx.paging.PagingData
import com.example.shared.domain.entity.Profile
import com.example.shared.domain.entity.ProfileCompact
import com.example.util.image.ImageInfo
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(profileId: String): Profile

    fun getProfileSubscribers(profileId: String): Flow<PagingData<ProfileCompact>>

    suspend fun subscribeOn(profileId: String)

    suspend fun unsubscribeOf(profileId: String)

    suspend fun updateProfile(displayName: String?, bio: String?, avatar: ImageInfo?)

    fun searchProfile(query: String): Flow<PagingData<ProfileCompact>>
}