package com.example.nanopost.data.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.core.graphics.decodeBitmap
import androidx.core.graphics.scale
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nanopost.data.remote.mappers.toDomainImage
import com.example.nanopost.data.remote.network.ApiService
import com.example.nanopost.data.remote.network.model.ImageInfo
import com.example.nanopost.data.remote.paging.BasePagingSource
import com.example.nanopost.domain.entity.Image
import com.example.nanopost.domain.repository.ImagesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

class ImagesRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val contentResolver: ContentResolver,
) : ImagesRepository {

    companion object {
        private const val MAX_IMAGE_SIZE = 2560
        private const val PAGE_SIZE = 30
    }

    override suspend fun getProfileImagesPreview(profileId: String): List<Image> {
        return apiService.getProfileImages(profileId, 4, null).items.map { it.toDomainImage() }
    }

    override fun getProfileImages(profileId: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getProfileImages(
                        profileId,
                        loadSize,
                        key
                    )
                })
            }
        ).flow.map { pagedData -> pagedData.map { it.toDomainImage() } }
    }

    override suspend fun deleteImage(imageId: String) {
        apiService.deleteImage(imageId)
    }

    override suspend fun getImage(imageId: String): Image {
        return apiService.getImage(imageId).toDomainImage()
    }

    override fun uriToImageInfo(uri: Uri): ImageInfo {
        return ImageInfo(
            name = getFileName(uri) ?: "default",
            mimeType = contentResolver.getType(uri),
            bytes = getBytes(uri)
        )
    }

    private fun getFileName(uri: Uri): String? {
        return contentResolver.query(
            uri,
            arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
            null,
            null,
            null,
        )?.use { cursor ->
            val index: Int = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
            cursor.moveToNext()
            cursor.getString(index)
        }
    }

    private fun getBytes(uri: Uri): ByteArray {
        val bitmap = ImageDecoder.createSource(contentResolver, uri).decodeBitmap { _, _ -> }
        val outputStream = ByteArrayOutputStream()
        var width = bitmap.width
        var height = bitmap.height
        if (width > MAX_IMAGE_SIZE || height > MAX_IMAGE_SIZE) {
            if (width > height) {
                height = ((MAX_IMAGE_SIZE / width.toFloat()) * height).toInt()
                width = MAX_IMAGE_SIZE
            } else {
                width = ((MAX_IMAGE_SIZE / height.toFloat()) * width).toInt()
                height = MAX_IMAGE_SIZE
            }
            bitmap.scale(width, height)
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        return outputStream.toByteArray()
    }
}