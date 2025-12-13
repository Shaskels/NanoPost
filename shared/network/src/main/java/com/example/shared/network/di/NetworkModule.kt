package com.example.shared.network.di

import com.example.shared.network.data.network.AuthService
import com.example.shared.network.domain.exceptions.AuthenticationException
import com.example.shared.network.domain.exceptions.InternetProblemException
import com.example.shared.network.domain.exceptions.WrongPasswordException
import com.example.shared.settings.data.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@Qualifier
annotation class AuthClient

@Qualifier
annotation class ApiClient

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl(): String {
        return "https://nanopost.evolitist.com/api"
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJsonConverter() = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideBearerAuthProvider(
        settingsDataStore: SettingsDataStore,
        authService: AuthService,
    ) = BearerAuthProvider(
        realm = null,
        loadTokens = {
            val accessToken = settingsDataStore.getAccessToken()
            accessToken?.let { BearerTokens(accessToken, "") }
        },
        refreshTokens = {
            val username = settingsDataStore.getUsername()
            val password = settingsDataStore.getPassword()
            if (username != null && password != null) {
                val token = authService.loginUser(username, password)
                settingsDataStore.setAccessToken(token.token)
                settingsDataStore.setUserId(token.userId)
                BearerTokens(token.token, "")
            } else {
                throw AuthenticationException("Authentication failed")
            }
        },
    )

    @Provides
    @Singleton
    @AuthClient
    fun provideAuthClient(
        json: Json,
    ) = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.ANDROID
        }

        install(ContentNegotiation) {
            json(json)
        }

        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value

                when (statusCode) {
                    HttpStatusCode.BadRequest.value -> throw WrongPasswordException("Wrong password")
                    HttpStatusCode.InternalServerError.value -> throw InternetProblemException("Internet problem")
                    HttpStatusCode.GatewayTimeout.value -> throw InternetProblemException("Internet problem")
                }
            }
        }
    }

    @Provides
    @Singleton
    @ApiClient
    fun provideApiClient(
        json: Json,
        bearerAuthProvider: BearerAuthProvider,
    ) = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.ANDROID
        }

        install(Auth) {
            providers.add(bearerAuthProvider)
        }

        install(ContentNegotiation) {
            json(json)
        }

        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value

                when (statusCode) {
                    HttpStatusCode.BadRequest.value -> throw WrongPasswordException("Wrong password")
                    HttpStatusCode.InternalServerError.value -> throw InternetProblemException("Internet problem")
                    HttpStatusCode.GatewayTimeout.value -> throw InternetProblemException("Internet problem")
                    HttpStatusCode.Unauthorized.value -> throw AuthenticationException("Authentication failed")
                }
            }
        }
    }

}