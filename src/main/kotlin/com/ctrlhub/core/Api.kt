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

class Api private constructor(httpClient: HttpClient) {
    companion object {
        fun create(): Api {
            val httpClient = KtorClientFactory.create()
            return Api(configureHttpClient(httpClient, Config.apiBaseUrl))
        }

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