package com.example.nanopost.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.example.nanopost.domain.entity.Image
import com.example.shared.network.data.network.model.ImageInfo
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    suspend fun getProfileImagesPreview(profileId: String): List<Image>

    fun getProfileImages(profileId: String): Flow<PagingData<Image>>

    suspend fun deleteImage(imageId: String)

    suspend fun getImage(imageId: String): Image

    fun uriToImageInfo(uri: Uri): ImageInfo
}