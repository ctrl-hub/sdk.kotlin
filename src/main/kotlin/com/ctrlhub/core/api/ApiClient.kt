package com.ctrlhub.core.api

import io.ktor.client.statement.HttpResponse

interface ApiClient {
    suspend fun get(url: String, headers: Map<String, String>? = null): HttpResponse
    suspend fun post(url: String, body: Any, headers: Map<String, String>? = null): HttpResponse
    suspend fun put(url: String, body: Any, headers: Map<String, String>? = null): HttpResponse
    suspend fun delete(url: String, headers: Map<String, String>? = null): HttpResponse
}
