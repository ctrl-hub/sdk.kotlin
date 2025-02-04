package com.ctrlhub.core.router

import com.ctrlhub.core.api.MissingSessionTokenException
import com.ctrlhub.core.router.request.JsonApiIncludes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers

abstract class Router(val httpClient: HttpClient, val requiresAuthentication: Boolean) {
    var sessionToken: String? = null

    protected fun buildIncludesQueryString(vararg includes: JsonApiIncludes): String {
        if (includes.isEmpty()) {
            return ""
        }
        return "include=" + includes.joinToString(",") { it.value().lowercase() }
    }

    protected suspend fun performGet(endpoint: String): HttpResponse {
        if (requiresAuthentication && sessionToken.isNullOrEmpty()) {
            throw MissingSessionTokenException()
        }

        return httpClient.get(endpoint) {
            if (requiresAuthentication && !sessionToken.isNullOrEmpty()) headers {
                header("X-Session-Token", sessionToken!!)
            }
        }
    }

    protected suspend inline fun <reified T> performPost(endpoint: String, body: T): HttpResponse {
        if (requiresAuthentication && sessionToken.isNullOrEmpty()) {
            throw MissingSessionTokenException()
        }
        return httpClient.post(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(body)

            if (requiresAuthentication && !sessionToken.isNullOrEmpty()) headers {
                header("X-Session-Token", sessionToken!!)
            }
        }
    }

    protected suspend inline fun <reified T> performDelete(endpoint: String, body: T): HttpResponse {
        if (requiresAuthentication && sessionToken == null) {
            throw MissingSessionTokenException()
        }

        return httpClient.delete(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(body)

            if (requiresAuthentication && !sessionToken.isNullOrEmpty()) headers {
                header("X-Session-Token", sessionToken!!)
            }
        }
    }

    protected suspend inline fun <reified T> performDelete(endpoint: String): HttpResponse {
        if (requiresAuthentication && sessionToken == null) {
            throw MissingSessionTokenException()
        }

        return httpClient.delete(endpoint) {
            contentType(ContentType.Application.Json)

            if (requiresAuthentication && !sessionToken.isNullOrEmpty()) headers {
                header("X-Session-Token", sessionToken!!)
            }
        }
    }
}