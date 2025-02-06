package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.*

/**
 * The facade object through which interaction with the API occurs.
 */
class Api(
    var httpClient: HttpClient = KtorClientFactory.create()
) {
    private var sessionToken: String? = null

    fun withHttpClientConfig(config: HttpClientConfig<*>.() -> Unit) {
        httpClient = KtorClientFactory.create(configBlock = config)
    }

    fun applySessionToken(newSessionToken: String) {
        sessionToken = newSessionToken
        httpClient = KtorClientFactory.createWithExistingConfig(httpClient, newSessionToken)
    }
}