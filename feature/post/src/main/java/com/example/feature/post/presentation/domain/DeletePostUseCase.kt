package com.example.feature.post.presentation.domain

import com.example.shared.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String) {
        postRepository.deletePost(postId)
    }
}