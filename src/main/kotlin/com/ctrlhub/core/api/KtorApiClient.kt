package com.ctrlhub.core.api

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorApiClient private constructor(val httpClient: HttpClient) {
    companion object {
        fun create(baseUrl: String): KtorApiClient {
            val httpClient = HttpClient(CIO)
            return KtorApiClient(configureHttpClient(httpClient, baseUrl))
        }

        fun create(httpClient: HttpClient): KtorApiClient {
            return KtorApiClient(configureHttpClient(httpClient))
        }

        private fun configureHttpClient(baseClient: HttpClient, baseUrl: String? = null): HttpClient {
            return baseClient.config {
                baseUrl?.let {
                    defaultRequest {
                        url(baseUrl)
                    }
                }
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
        }

    }

    suspend fun get(
        url: String,
        headers: Map<String, String>? = null
    ): HttpResponse {
        return httpClient.get(url) {
            headers?.forEach { (key, value) -> header(key, value) }
        }
    }

    suspend inline fun <reified T> post(
        url: String,
        body: T?,
        headers: Map<String, String>? = null
    ): HttpResponse {
        return httpClient.post(url) {
            headers?.forEach { (key, value) -> header(key, value) }
            body?.let { setBody(Json.encodeToString<T>(it)) }
        }
    }

    suspend inline fun <reified T> put(
        url: String,
        body: T?,
        headers: Map<String, String>? = null
    ): HttpResponse {
        return httpClient.put(url) {
            headers?.forEach { (key, value) -> header(key, value) }
            body?.let { setBody(Json.encodeToString<T>(it)) }
        }
    }

    suspend fun delete(
        url: String,
        headers: Map<String, String>? = null
    ): HttpResponse {
        return httpClient.delete(url) {
            headers?.forEach { (key, value) ->
                header(key, value)
            }
        }
    }
}