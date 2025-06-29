package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.*
import io.ktor.client.plugins.defaultRequest

/**
 * The facade object through which interaction with the API occurs.
 */
class Api(
    var httpClient: HttpClient
) {
    fun withHttpClientConfig(config: HttpClientConfig<*>.() -> Unit) {
        httpClient = KtorClientFactory.create(configBlock = config)
    }

    fun applySessionToken(sessionToken: String) {
        httpClient = httpClient.config {
            defaultRequest {
                headers["X-Session-Token"] = sessionToken
            }
        }
    }

    fun clearSessionToken() {
        httpClient = httpClient.config {
            defaultRequest {
                headers.remove("X-Session-Token")
            }
        }
    }
}