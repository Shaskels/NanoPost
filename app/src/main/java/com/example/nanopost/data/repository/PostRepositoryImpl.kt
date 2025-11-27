package com.example.nanopost.data.repository

import com.example.nanopost.data.remote.ApiService
import com.example.nanopost.data.remote.mappers.toDomainPost
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import jakarta.inject.Inject

class PostRepositoryImpl @Inject constructor(private val apiService: ApiService): PostRepository {

    override suspend fun getPosts(): List<Post> {
        val feed = apiService.getFeed()
        return feed.items.map { it.toDomainPost() }
    }
}