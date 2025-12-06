package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import com.example.nanopost.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): List<Post> {
        return withContext(Dispatchers.IO){
            val userId = settingsRepository.getUserId()
            postRepository.getProfilePosts(userId)
        }
    }
}