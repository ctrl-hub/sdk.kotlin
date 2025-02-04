package com.ctrlhub.core.api

import io.ktor.client.statement.*

/**
 * Represents an exception that occurred when interacting with the API
 */
open class ApiException(message: String, e: Throwable) : Exception(message, e)

/**
 * Represents a client based exception that occurred when interacting with the API.
 * This is usually the result of a non-200 HTTP response code.
 */
class ApiClientException(message: String, val response: HttpResponse, e: Throwable) : ApiException(message, e) {
    fun statusCode(): Int {
        return response.status.value
    }
}

class MissingSessionTokenException : Exception("No session token provided")