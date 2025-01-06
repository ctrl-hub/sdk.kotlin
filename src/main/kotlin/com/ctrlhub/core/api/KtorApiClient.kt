package com.ctrlhub.core.api

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorApiClient(engine: HttpClientEngine, val baseUrl: String) {
    val httpClient = HttpClient(engine) {
        defaultRequest {
            url(baseUrl)
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true})
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
            body?.let { setBody(Json.encodeToString(it)) }
        }
    }

    suspend fun put(
        url: String,
        body: Any,
        headers: Map<String, String>?
    ): HttpResponse {
        return httpClient.put(url) {
            headers?.forEach { (key, value) -> header(key, value) }
            setBody(Json.encodeToString(body))
        }
    }

    suspend fun delete(
        url: String,
        headers: Map<String, String>?
    ): HttpResponse {
        return httpClient.delete(url) {
            headers?.forEach { (key, value) ->
                header(key, value)
            }
        }
    }
}