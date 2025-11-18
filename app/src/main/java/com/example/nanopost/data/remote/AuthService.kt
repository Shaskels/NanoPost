package com.example.nanopost.data.remote

import com.example.nanopost.data.remote.model.RegisterRequest
import com.example.nanopost.data.remote.model.TokenResponse
import com.example.nanopost.data.remote.model.UsernameCheckResponse
import com.example.nanopost.di.AuthClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthService @Inject constructor(
    private val baseUrl: String,
    @AuthClient private val httpClient: HttpClient
) {

    suspend fun checkUsername(username: String): UsernameCheckResponse =
        httpClient.get("$baseUrl/auth/checkUsername") {
            parameter("username", username)
        }.body()

    suspend fun loginUser(username: String, password: String): TokenResponse =
        httpClient.get("$baseUrl/auth/login") {
            parameter("username", username)
            parameter("password", password)
        }.body()

    suspend fun registerUser(registerRequest: RegisterRequest): TokenResponse =
        httpClient.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }.body()
}