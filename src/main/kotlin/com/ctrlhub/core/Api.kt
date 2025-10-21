package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.*
import io.ktor.client.plugins.defaultRequest
import com.ctrlhub.core.ResourceTypeRegistry

/**
 * The facade object through which interaction with the API occurs.
 */
class Api(
    var httpClient: HttpClient = KtorClientFactory.create()
) {
    init {
        ResourceTypeRegistry.registerDefaults()
    }

    fun withHttpClientConfig(config: HttpClientConfig<*>.() -> Unit) {
        httpClient = KtorClientFactory.create(configBlock = config)
    }

    fun applySessionToken(sessionToken: String) {
        httpClient = KtorClientFactory.create(httpClient) {
            defaultRequest {
                headers["X-Session-Token"] = sessionToken
            }
        }
    }

    fun clearSessionToken() {
        httpClient = KtorClientFactory.create(httpClient) {
            defaultRequest {
                headers.remove("X-Session-Token")
            }
        }
    }
}