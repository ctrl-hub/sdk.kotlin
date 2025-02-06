package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.*

/**
 * The facade object through which interaction with the API occurs.
 */
object Api {
    private var sessionToken: String? = null
    var httpClient: HttpClient = KtorClientFactory.create()
        private set

    fun withHttpClientConfig(config: HttpClientConfig<*>.() -> Unit) {
        httpClient = KtorClientFactory.create(configBlock = config)
    }

    fun applySessionToken(newSessionToken: String) {
        sessionToken = newSessionToken
        httpClient = KtorClientFactory.createWithExistingConfig(httpClient, newSessionToken)
    }
}