package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.*
import io.ktor.client.plugins.defaultRequest

/**
 * The facade object through which interaction with the API occurs.
 */
class Api(
    var httpClient: HttpClient = KtorClientFactory.create()
) {
    fun withHttpClientConfig(config: HttpClientConfig<*>.() -> Unit) {
        httpClient = KtorClientFactory.create(configBlock = config)
    }

    fun applySessionToken(sessionToken: String) {
        httpClient = KtorClientFactory.create(httpClient) {
            defaultRequest {
                headers.append("X-Session-Token", sessionToken)
            }
        }
    }
}