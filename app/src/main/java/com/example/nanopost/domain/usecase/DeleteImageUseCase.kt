package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.repository.ImagesRepository
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    suspend operator fun invoke(imageId: String) {
        imagesRepository.deleteImage(imageId)
    }
}