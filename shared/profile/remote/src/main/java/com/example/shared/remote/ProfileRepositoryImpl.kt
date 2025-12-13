package com.example.shared.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.shared.domain.entity.Profile
import com.example.shared.domain.entity.ProfileCompact
import com.example.shared.domain.repository.ProfileRepository
import com.example.shared.network.data.network.ApiService
import com.example.shared.network.data.network.model.ProfileCompactModel
import com.example.shared.network.data.paging.BasePagingSource
import com.example.util.image.ImageInfo
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
        val pager: Pager<String, ProfileCompactModel> = Pager(
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
        )
        return pager.flow.map { pagingSource -> pagingSource.map { it.toDomainProfileCompact() } }
    }

    override suspend fun subscribeOn(profileId: String) {
        apiService.subscribeOn(profileId)
    }

    override suspend fun unsubscribeOf(profileId: String) {
        apiService.unsubscribeOf(profileId)
    }

    override suspend fun updateProfile(
        displayName: String?,
        bio: String?,
        avatar: ImageInfo?
    ) {
        apiService.patchProfile(displayName, bio, avatar)
    }

    override fun searchProfile(query: String): Flow<PagingData<ProfileCompact>> {
        val pager: Pager<String, ProfileCompactModel> = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.searchProfile(
                        query,
                        loadSize,
                        key
                    )
                })
            }
        )
        return pager.flow.map { pagingSource -> pagingSource.map { it.toDomainProfileCompact() } }
    }
}