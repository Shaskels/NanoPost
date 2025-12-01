package com.example.nanopost.data.remote

import com.example.nanopost.data.remote.model.FeedResponse
import com.example.nanopost.di.ApiClient
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import javax.inject.Inject


class ApiService @Inject constructor(
    baseUrl: String,
    @ApiClient httpClient: HttpClient,
): BaseService(httpClient, baseUrl) {

    suspend fun getFeed(): FeedResponse = get("/v1/feed") {
        parameter("count", 30)
        parameter("offset", 0)
        parameter("mode", "full")
    }
}