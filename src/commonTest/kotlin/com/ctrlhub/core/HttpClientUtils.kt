package com.ctrlhub.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

fun HttpClient.configureForTest(): HttpClient {
    return this.config {
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