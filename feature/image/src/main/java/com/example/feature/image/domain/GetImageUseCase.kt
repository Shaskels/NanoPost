package com.example.feature.image.domain

import com.example.shared.domain.entity.Image
import com.example.shared.domain.repository.ImagesRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    suspend operator fun invoke(imageId: String): Image {
        return imagesRepository.getImage(imageId)
    }
}