package com.example.nanopost.domain.usecase

import androidx.paging.PagingData
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(private val postRepository: PostRepository) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return postRepository.getFeed()
    }
}