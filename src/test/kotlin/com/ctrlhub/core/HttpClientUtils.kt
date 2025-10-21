package com.ctrlhub.core

import com.ctrlhub.core.ResourceTypeRegistry
import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

fun HttpClient.configureForTest(): HttpClient {
    // ensure test environment has default resource type registrations
    ResourceTypeRegistry.registerDefaults()
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