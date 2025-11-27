package com.example.nanopost.domain.repository

import com.example.nanopost.domain.entity.Post

interface PostRepository {

    suspend fun getPosts(): List<Post>

}