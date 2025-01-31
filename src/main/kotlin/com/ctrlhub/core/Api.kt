package com.ctrlhub.core

import com.ctrlhub.core.http.KtorClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

/**
 * The "facade" class through which interaction with the API occurs.
 */
class Api private constructor(httpClient: HttpClient) {
    companion object {
        /**
         * Creates a new Api instance with a default client
         */
        fun create(): Api {
            val httpClient = KtorClientFactory.create()
            return Api(configureHttpClient(httpClient, Config.apiBaseUrl))
        }

        /**
         * Creates a new Api client, with a given HttpClient instance.
         * Default config will be applied to this client
         *
         * @param httpClient HttpClient A ktor client instance
         */
        fun create(httpClient: HttpClient): Api {
            return Api(httpClient)
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
}