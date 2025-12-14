package com.examle.feature.images.domain

import androidx.paging.PagingData
import com.example.shared.domain.entity.Image
import com.example.shared.domain.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    operator fun invoke(profileId: String): Flow<PagingData<Image>> {
        return imagesRepository.getProfileImages(profileId)
    }
}