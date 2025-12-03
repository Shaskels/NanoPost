package com.example.nanopost.domain.usecase

import android.net.Uri
import com.example.nanopost.domain.repository.PostRepository
import jakarta.inject.Inject

class UploadPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(text: String?, images: List<Uri>) {
        postRepository.putPost(text, images)
    }
}