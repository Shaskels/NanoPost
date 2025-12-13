package com.example.shared.network.data.network

import com.example.shared.network.data.network.model.RegisterRequest
import com.example.shared.network.data.network.model.TokenResponse
import com.example.shared.network.data.network.model.UsernameCheckResponse
import com.example.shared.network.di.AuthClient
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthService @Inject constructor(
    baseUrl: String,
    @AuthClient httpClient: HttpClient
) : BaseService(httpClient, baseUrl) {

    suspend fun checkUsername(username: String): UsernameCheckResponse =
        get("/auth/checkUsername") {
            parameter("username", username)
        }

    suspend fun loginUser(username: String, password: String): TokenResponse =
        get("/auth/login") {
            parameter("username", username)
            parameter("password", password)
        }

    suspend fun registerUser(registerRequest: RegisterRequest): TokenResponse =
        post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }
}