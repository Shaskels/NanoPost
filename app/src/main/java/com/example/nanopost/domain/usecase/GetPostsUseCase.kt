package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(): List<Post> {
        return postRepository.getPosts()
    }
}