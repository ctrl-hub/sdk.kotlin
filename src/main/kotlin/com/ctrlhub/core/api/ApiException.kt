package com.ctrlhub.core.api

import io.ktor.client.statement.*

/**
 * Represents an exception that occurred when interacting with the API
 */
open class ApiException : Exception {
    constructor(message: String, e: Throwable) : super(message, e)
    constructor(message: String) : super(message)
}

/**
 * Represents an authorized error that occurs when interacting with the API.
 */
class UnauthorizedException(message: String, val response: HttpResponse, e: Throwable) : ApiException(message, e)

/**
 * Represents a client based exception that occurred when interacting with the API.
 * This is usually the result of a non-200 HTTP response code.
 */
class ApiClientException(message: String, val response: HttpResponse, e: Throwable) : ApiException(message, e) {
    fun statusCode(): Int {
        return response.status.value
    }
}