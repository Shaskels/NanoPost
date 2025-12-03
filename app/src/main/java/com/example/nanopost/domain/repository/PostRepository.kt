package com.example.nanopost.domain.repository

import android.net.Uri
import com.example.nanopost.domain.entity.Post

interface PostRepository {

    suspend fun getPosts(): List<Post>

    suspend fun putPost(text: String?, images: List<Uri>): Post

    suspend fun likePost(postId: String)

    suspend fun unlikePost(postId: String)
}