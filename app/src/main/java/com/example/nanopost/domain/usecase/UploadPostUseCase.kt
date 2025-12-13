package com.example.nanopost.domain.usecase

import android.net.Uri
import com.example.shared.domain.repository.ImagesRepository
import com.example.shared.domain.repository.PostRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UploadPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val imagesRepository: ImagesRepository
) {

    suspend operator fun invoke(text: String?, images: List<Uri>) {
        withContext(Dispatchers.Default) {
            postRepository.putPost(text, images.map { imagesRepository.uriToImageInfo(it) })
        }
    }
}