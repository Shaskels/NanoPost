package com.example.nanopost.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nanopost.data.remote.network.ApiService
import com.example.nanopost.data.remote.paging.BasePagingSource
import com.example.nanopost.data.remote.mappers.toDomainProfile
import com.example.nanopost.data.remote.mappers.toDomainProfileCompact
import com.example.nanopost.domain.entity.Profile
import com.example.nanopost.domain.entity.ProfileCompact
import com.example.nanopost.domain.repository.ProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {

    companion object {
        private const val PAGE_SIZE = 30
    }

    override suspend fun getProfile(profileId: String): Profile {
        return apiService.getProfile(profileId).toDomainProfile()
    }

    override fun getProfileSubscribers(profileId: String): Flow<PagingData<ProfileCompact>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getProfileSubscribers(
                        profileId,
                        loadSize,
                        key
                    )
                })
            }
        ).flow.map { pagingSource -> pagingSource.map { it.toDomainProfileCompact() } }
    }

    override suspend fun subscribeOn(profileId: String) {
        apiService.subscribeOn(profileId)
    }

    override suspend fun unsubscribeOf(profileId: String) {
        apiService.unsubscribeOf(profileId)
    }


}