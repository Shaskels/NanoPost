package com.example.nanopost.data.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.core.graphics.decodeBitmap
import androidx.core.graphics.scale
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nanopost.data.remote.network.ApiService
import com.example.nanopost.data.remote.paging.BasePagingSource
import com.example.nanopost.data.remote.mappers.toDomainPost
import com.example.nanopost.data.remote.network.model.ImageInfo
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val contentResolver: ContentResolver,
) : PostRepository {

    companion object {
        private const val MAX_IMAGE_SIZE = 2560
        private const val PAGE_SIZE = 30
    }

    override fun getFeed(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getFeed(
                        loadSize,
                        key
                    )
                })
            }
        ).flow.map { pagingSource -> pagingSource.map { it.toDomainPost() } }
    }

    override suspend fun putPost(text: String?, images: List<ImageInfo>): Post {
        return apiService.putPost(text, images).toDomainPost()
    }

    override suspend fun getPost(postId: String): Post {
        return apiService.getPost(postId).toDomainPost()
    }

    override suspend fun deletePost(postId: String) {
        apiService.deletePost(postId)
    }

    override suspend fun likePost(postId: String) {
        apiService.likePost(postId)
    }

    override suspend fun unlikePost(postId: String) {
        apiService.unlikePost(postId)
    }

    override suspend fun getProfilePosts(profileId: String): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getProfilePosts(
                        profileId,
                        loadSize,
                        key
                    )
                })
            }
        ).flow.map { pagingSource -> pagingSource.map { it.toDomainPost() } }
    }
}