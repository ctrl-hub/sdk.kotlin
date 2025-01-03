package com.ctrlhub.core.router

import io.ktor.http.HttpMethod

sealed class NetworkRequest<T>(
    val url: String,
    val method: HttpMethod = HttpMethod.Get,
    val body: T? = null,
    val headers: Map<String, String> = emptyMap()
)

sealed class NetworkResult<out T>() {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String): NetworkResult<Nothing>()
}