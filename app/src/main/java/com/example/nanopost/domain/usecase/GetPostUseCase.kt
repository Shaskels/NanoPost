package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): Post {
        return postRepository.getPost(postId)
    }
}