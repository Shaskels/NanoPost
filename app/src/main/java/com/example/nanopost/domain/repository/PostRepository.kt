package com.example.nanopost.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.example.nanopost.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun getFeed(): List<Post>

    suspend fun putPost(text: String?, images: List<Uri>): Post

    suspend fun getPost(postId: String): Post

    suspend fun deletePost(postId: String)

    suspend fun likePost(postId: String)

    suspend fun unlikePost(postId: String)

    suspend fun getProfilePosts(profileId: String): Flow<PagingData<Post>>
}