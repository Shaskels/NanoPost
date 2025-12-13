package com.example.nanopost.domain.usecase

import com.example.shared.domain.entity.Post
import com.example.shared.domain.repository.PostRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): Post {
        return postRepository.getPost(postId)
    }
}