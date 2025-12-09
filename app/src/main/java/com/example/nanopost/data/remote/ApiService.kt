package com.example.nanopost.data.remote

import com.example.nanopost.data.remote.model.FeedResponse
import com.example.nanopost.data.remote.model.ImageInfo
import com.example.nanopost.data.remote.model.PostModel
import com.example.nanopost.data.remote.model.ProfileImagesResponse
import com.example.nanopost.data.remote.model.ProfileModel
import com.example.nanopost.data.remote.model.ProfilePostsResponse
import com.example.nanopost.di.ApiClient
import com.example.nanopost.domain.entity.Post
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.utils.io.core.writeFully
import okhttp3.Response
import javax.inject.Inject

class ApiService @Inject constructor(
    baseUrl: String,
    @ApiClient httpClient: HttpClient,
) : BaseService(httpClient, baseUrl) {

    suspend fun getFeed(): FeedResponse = get("/v1/feed") {
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
                            contentType = images.mimeType?.let { ContentType.parse(it) },
                            bodyBuilder = { writeFully(images.bytes) }
                        )
                    }
                }
            )
        )
    }

    suspend fun getPost(postId: String): PostModel = get("/v1/post/$postId")

    suspend fun deletePost(postId: String) = delete<Response>("/api/v1/post/$postId")

    suspend fun likePost(postId: String): PostModel = put("/v1/post/$postId/like")

    suspend fun unlikePost(postId: String): PostModel = delete("/v1/post/$postId/like")

    suspend fun getProfile(profileId: String): ProfileModel = get("/v1/profile/$profileId")

    suspend fun getProfileImages(profileId: String): ProfileImagesResponse =
        get("/v1/images/$profileId")

    suspend fun getProfilePosts(
        profileId: String,
        count: Int,
        offset: String?
    ): ProfilePostsResponse = get("/v1/posts/$profileId") {
        parameter("count", count)
        offset?.let { parameter("offset", offset) }
    }
}