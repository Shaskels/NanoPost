package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String) {
        postRepository.deletePost(postId)
    }
}