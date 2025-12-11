package com.example.nanopost.data.remote.network

import com.example.nanopost.data.remote.network.BaseService
import com.example.nanopost.data.remote.network.model.ImageInfo
import com.example.nanopost.data.remote.network.model.ImageModel
import com.example.nanopost.data.remote.network.model.PagedResponse
import com.example.nanopost.data.remote.network.model.PostModel
import com.example.nanopost.data.remote.network.model.ProfileCompactModel
import com.example.nanopost.data.remote.network.model.ProfileModel
import com.example.nanopost.di.ApiClient
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.utils.io.core.writeFully
import javax.inject.Inject

class ApiService @Inject constructor(
    baseUrl: String,
    @ApiClient httpClient: HttpClient,
) : BaseService(httpClient, baseUrl) {

    suspend fun getFeed(): PagedResponse<PostModel> = get("/v1/feed") {
        parameter("count", 30)
        parameter("offset", 0)
        parameter("mode", "full")
    }

    suspend fun putPost(
        text: String?,
        images: List<ImageInfo>
    ): PostModel = put("/v1/post") {
        setBody(
            MultiPartFormDataContent(
                parts = formData {
                    text?.let { append("text", text) }
                    images.forEachIndexed { index, images ->
                        append(
                            key = "image$index",
                            filename = images.name,
                            contentType = images.mimeType?.let { ContentType.Companion.parse(it) },
                            bodyBuilder = { writeFully(images.bytes) }
                        )
                    }
                }
            )
        )
    }

    suspend fun getPost(postId: String): PostModel = get("/v1/post/$postId")

    suspend fun deletePost(postId: String) = delete<Unit>("/v1/post/$postId")

    suspend fun likePost(postId: String): PostModel = put("/v1/post/$postId/like")

    suspend fun unlikePost(postId: String): PostModel = delete("/v1/post/$postId/like")

    suspend fun getProfile(profileId: String): ProfileModel = get("/v1/profile/$profileId")

    suspend fun deleteImage(imageId: String) = delete<Unit>("/v1/image/$imageId")

    suspend fun getImage(imageId: String) = get<ImageModel>("/v1/image/$imageId")

    suspend fun getProfileImages(
        profileId: String,
        count: Int,
        offset: String?
    ): PagedResponse<ImageModel> =
        get("/v1/images/$profileId") {
            parameter("count", count)
            offset?.let { parameter("offset", offset) }
        }

    suspend fun getProfilePosts(
        profileId: String,
        count: Int,
        offset: String?
    ): PagedResponse<PostModel> = get("/v1/posts/$profileId") {
        parameter("count", count)
        offset?.let { parameter("offset", offset) }
    }

    suspend fun getProfileSubscribers(
        profileId: String,
        count: Int,
        offset: String?,
    ): PagedResponse<ProfileCompactModel> = get("/v1/subscribers/$profileId") {
        parameter("count", count)
        offset?.let { parameter("offset", offset) }
    }
}