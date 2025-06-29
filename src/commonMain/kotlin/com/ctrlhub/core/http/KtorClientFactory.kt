package com.ctrlhub.core.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.serialization.json.Json

expect fun getHttpEngine(): HttpClientEngineFactory<*>

object KtorClientFactory {
    fun create(
        configBlock: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        return HttpClient(getHttpEngine()) {
            defaultRequest {
                // Adjust as needed for your API base config
                headers.append(HttpHeaders.ContentType, "application/json")
            }
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            install(UserAgent) {
                agent = "CtrlHub-KMP-Client"
            }
            configBlock()
        }
    }
}