package com.example.nanopost.domain.usecase

import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.repository.ImagesRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    suspend operator fun invoke(imageId: String): Image {
        return imagesRepository.getImage(imageId)
    }
}