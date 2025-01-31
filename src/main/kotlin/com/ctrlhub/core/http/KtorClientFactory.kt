package com.ctrlhub.core.http

import com.ctrlhub.core.Config
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

/**
 * A wrapper around the Ktor client. This allows default configuration
 * to be applied that is specific to the Ctrl Hub API, but is also flexible
 * in that an existing Ktor client can be passed, and config is applied to that.
 */
object KtorClientFactory {
    private lateinit var httpClient: HttpClient

    fun create(): HttpClient {
        if (!this::httpClient.isInitialized) {
            val httpClient = HttpClient(CIO)
            this.httpClient = configureHttpClient(httpClient)
        }

        return this.httpClient
    }

    fun create(httpClient: HttpClient): HttpClient {
        this.httpClient = httpClient
        return configureHttpClient(httpClient, Config.apiBaseUrl)
    }

    private fun configureHttpClient(baseClient: HttpClient, baseUrl: String? = null): HttpClient {
        return baseClient.config {
            baseUrl?.let {
                defaultRequest {
                    url(baseUrl)
                }
            }
            defaultRequest {
                headers.appendIfNameAbsent(HttpHeaders.ContentType, "application/json")
            }
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(UserAgent) {
                agent = Config.userAgent
            }
        }
    }
}