package com.ctrlhub.core.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorApiClient(val baseUrl: String) : ApiClient {
    private val httpClient by lazy {
        HttpClient(CIO) {
            defaultRequest {
                url(baseUrl)
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true})
            }
        }
    }

    override suspend fun get(
        url: String,
        headers: Map<String, String>?
    ): HttpResponse {
        return httpClient.get(url) {
            headers?.forEach { (key, value) -> header(key, value) }
        }
    }

    override suspend fun post(
        url: String,
        body: Any,
        headers: Map<String, String>?
    ): HttpResponse {
        return httpClient.post(url) {
            headers?.forEach { (key, value) -> header(key, value) }
            setBody(Json.encodeToString(body))
        }
    }

    override suspend fun put(
        url: String,
        body: Any,
        headers: Map<String, String>?
    ): HttpResponse {
        return httpClient.put(url) {
            headers?.forEach { (key, value) -> header(key, value) }
            setBody(Json.encodeToString(body))
        }
    }

    override suspend fun delete(
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