package com.ctrlhub.core.api

import io.ktor.client.statement.HttpResponse

interface ApiClient {
    suspend fun <T> get(url: String, headers: Map<String, String>? = null): HttpResponse
    suspend fun <T> post(url: String, body: Any, headers: Map<String, String>? = null): HttpResponse
    suspend fun <T> put(url: String, body: Any, headers: Map<String, String>? = null): HttpResponse
    suspend fun <T> delete(url: String, headers: Map<String, String>? = null): HttpResponse
}
