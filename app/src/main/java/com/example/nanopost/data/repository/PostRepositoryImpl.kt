package com.example.nanopost.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nanopost.data.remote.mappers.toDomainPost
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import com.example.shared.network.data.network.ApiService
import com.example.shared.network.data.network.model.PostModel
import com.example.shared.network.data.paging.BasePagingSource
import com.example.util.image.ImageInfo
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : PostRepository {

    companion object {
        private const val PAGE_SIZE = 30
    }

    override fun getFeed(): Flow<PagingData<Post>> {
        val pager: Pager<String, PostModel> = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getFeed(
                        loadSize,
                        key
                    )
                })
            }
        )
        return pager.flow.map { pagingSource -> pagingSource.map { it -> it.toDomainPost() } }
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
        val pager: Pager<String, PostModel> = Pager(
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
        )
        return pager.flow.map { pagingSource -> pagingSource.map { it.toDomainPost() } }
    }
}