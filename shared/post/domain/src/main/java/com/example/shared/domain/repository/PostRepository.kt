package com.example.shared.domain.repository

import androidx.paging.PagingData
import com.example.shared.domain.entity.Post
import com.example.util.image.ImageInfo
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getFeed(): Flow<PagingData<Post>>

    suspend fun putPost(text: String?, images: List<ImageInfo>): Post

    suspend fun getPost(postId: String): Post

    suspend fun deletePost(postId: String)

    suspend fun likePost(postId: String)

    suspend fun unlikePost(postId: String)

    suspend fun getProfilePosts(profileId: String): Flow<PagingData<Post>>
}