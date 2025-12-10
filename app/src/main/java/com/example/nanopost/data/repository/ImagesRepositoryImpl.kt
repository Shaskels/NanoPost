package com.example.nanopost.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nanopost.data.remote.ApiService
import com.example.nanopost.data.remote.BasePagingSource
import com.example.nanopost.data.remote.mappers.toDomainImage
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.repository.ImagesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImagesRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ImagesRepository {

    companion object {
        private const val PAGE_SIZE = 30
    }

    override suspend fun getProfileImagesPreview(profileId: String): List<Image> {
        return apiService.getProfileImages(profileId, 4, null).items.map { it.toDomainImage() }
    }

    override fun getProfileImages(profileId: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getProfileImages(
                        profileId,
                        loadSize,
                        key
                    )
                })
            }
        ).flow.map { pagedData -> pagedData.map { it.toDomainImage() } }
    }
}