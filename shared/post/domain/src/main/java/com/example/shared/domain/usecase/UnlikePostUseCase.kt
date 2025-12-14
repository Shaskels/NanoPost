package com.example.shared.domain.usecase

import com.example.shared.domain.repository.PostRepository
import javax.inject.Inject

class UnlikePostUseCase @Inject constructor(
    private val postRepository: PostRepository
){
    suspend operator fun invoke(postId: String) {
        postRepository.unlikePost(postId)
    }
}