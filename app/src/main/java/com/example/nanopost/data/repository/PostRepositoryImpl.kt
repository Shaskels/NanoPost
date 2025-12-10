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
import com.example.nanopost.data.remote.ApiService
import com.example.nanopost.data.remote.BasePagingSource
import com.example.nanopost.data.remote.mappers.toDomainPost
import com.example.nanopost.data.remote.model.ImageInfo
import com.example.nanopost.data.remote.model.PostModel
import com.example.nanopost.domain.entity.Post
import com.example.nanopost.domain.repository.PostRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val contentResolver: ContentResolver,
) : PostRepository {

    companion object {
        private const val MAX_IMAGE_SIZE = 2560
        private const val PAGE_SIZE = 30
    }

    override suspend fun getFeed(): List<Post> {
        val feed = apiService.getFeed()
        return feed.items.map { it.toDomainPost() }
    }

    override suspend fun putPost(text: String?, images: List<Uri>): Post {
        return apiService.putPost(text, images.map { imageToImageInfo(it) }).toDomainPost()
    }

    override suspend fun getPost(postId: String): Post {
        return apiService.getPost(postId).toDomainPost()
    }

    override suspend fun deletePost(postId: String) {
        apiService.deletePost(postId)
    }

    override suspend fun likePost(postId: String) {
        apiService.likePost(postId)
    }

    override suspend fun unlikePost(postId: String) {
        apiService.unlikePost(postId)
    }

    override suspend fun getProfilePosts(profileId: String): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                BasePagingSource(loadData = { loadSize, key ->
                    apiService.getProfilePosts(
                        profileId,
                        loadSize,
                        key
                    )
                })
            }
        ).flow.map { pagingSource -> pagingSource.map { it.toDomainPost() } }
    }

    private fun imageToImageInfo(uri: Uri) = ImageInfo(
        name = getFileName(uri) ?: "default",
        mimeType = contentResolver.getType(uri),
        bytes = getBytes(uri)
    )

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