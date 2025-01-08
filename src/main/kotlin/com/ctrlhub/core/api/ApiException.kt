package com.ctrlhub.core.api

import io.ktor.client.statement.*

open class ApiException(message: String, e: Throwable) : Exception(message, e)

class ApiClientException(message: String, val response: HttpResponse, e: Throwable) : ApiException(message, e) {
    fun statusCode(): Int {
        return response.status.value
    }
}