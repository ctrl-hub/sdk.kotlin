package com.ctrlhub.core

import com.ctrlhub.core.auth.AuthRouter
import com.ctrlhub.core.iam.IamRouter
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

class Api private constructor(httpClient: HttpClient) {
    val auth: AuthRouter = AuthRouter(httpClient)
    val iam: IamRouter = IamRouter(httpClient)

    companion object {
        fun create(): Api {
            val httpClient = HttpClient(CIO)
            return Api(configureHttpClient(httpClient, Config.apiBaseUrl))
        }

        fun create(httpClient: HttpClient): Api {
            return Api(configureHttpClient(httpClient, Config.apiBaseUrl))
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