package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.*

/**
 * The facade object through which interaction with the API occurs.
 */
object Api {
    var sessionToken: String? = null

    val httpClient: HttpClient by lazy {
        KtorClientFactory.create(sessionToken)
    }
}