package com.ctrlhub.core.http

import com.ctrlhub.core.Config
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicReference

/**
 * A wrapper around the Ktor client. This allows default configuration
 * to be applied that is specific to the Ctrl Hub API, but is also flexible
 * in that an existing Ktor client can be passed, and config is applied to that.
 */
object KtorClientFactory {
    fun create(
        httpClient: HttpClient = HttpClient(CIO),
        sessionToken: String? = null,
        configBlock: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        return configureHttpClient(httpClient, sessionToken, configBlock)
    }

    fun createWithExistingConfig(
        existingClient: HttpClient,
        sessionToken: String? = null
    ): HttpClient {
        return configureHttpClient(existingClient, sessionToken)
    }

    private fun configureHttpClient(
        baseClient: HttpClient,
        sessionToken: String? = null,
        configBlock: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        return baseClient.config {
            defaultRequest {
                url(Config.apiBaseUrl)
                sessionToken?.let { headers.append("X-Session-Token", it) }
                headers.appendIfNameAbsent(HttpHeaders.ContentType, "application/json")
            }
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            install(UserAgent) {
                agent = Config.userAgent
            }
            configBlock()
        }
    }
}