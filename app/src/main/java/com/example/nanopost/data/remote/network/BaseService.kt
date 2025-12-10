package com.example.nanopost.data.remote.network

import com.example.nanopost.domain.exceptions.InternetProblemException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

abstract class BaseService(
    protected val client: HttpClient,
    protected val baseUrl: String,
) {
    protected suspend inline fun <reified T> get(endpoint: String, block: HttpRequestBuilder.() -> Unit = {}): T {
        return request(endpoint, HttpMethod.Companion.Get, block)
    }

    protected suspend inline fun <reified T> post(endpoint: String, block: HttpRequestBuilder.() -> Unit): T {
        return request(endpoint, HttpMethod.Companion.Post, block)
    }

    protected suspend inline fun <reified T> put(endpoint: String, block: HttpRequestBuilder.() -> Unit = {}): T {
        return request(endpoint, HttpMethod.Companion.Put, block)
    }

    protected suspend inline fun <reified T> patch(endpoint: String, block: HttpRequestBuilder.() -> Unit): T {
        return request(endpoint, HttpMethod.Companion.Patch, block)
    }

    protected suspend inline fun <reified T> delete(endpoint: String, block: HttpRequestBuilder.() -> Unit = {}): T {
        return request(endpoint, HttpMethod.Companion.Delete, block)
    }

    protected suspend inline fun <reified T> request(
        endpoint: String,
        method: HttpMethod,
        block: HttpRequestBuilder.() -> Unit,
    ): T {
        val url = URLBuilder(baseUrl).appendPathSegments(endpoint).build()
        return client.safeRequest<T>(url) {
            this.method = method
            block()
        }
    }

    protected suspend inline fun <reified T> HttpClient.safeRequest(
        url: Url,
        block: HttpRequestBuilder.() -> Unit,
    ): T =
        try {
            val response = request(url) { block() }
            response.body()
        } catch (e: UnknownHostException) {
            throw InternetProblemException("Internet problem")
        } catch (e: SSLHandshakeException) {
            throw InternetProblemException("Internet problem")
        }
}