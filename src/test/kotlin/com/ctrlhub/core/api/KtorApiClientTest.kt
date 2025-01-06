package com.ctrlhub.core.api

import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorApiClientTest {

    @Serializable
    data class StubResponse(val status: String)

    @Test
    fun `test GET request success`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"status": "success"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        runBlocking {
            val httpClient = KtorApiClient(mockEngine, "https://example.com")
            val response: StubResponse = httpClient.get("/").body()
            assertEquals("success", response.status)
        }
    }

    @Test
    fun `test POST request success`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"status": "success"}"""),
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        runBlocking {
            val httpClient = KtorApiClient(mockEngine, "https://example.com")
            val httpResponse = httpClient.post(url = "/", body = "")
            val response: StubResponse = httpResponse.body()
            assertEquals("success", response.status)
            assertEquals(HttpStatusCode.Created, httpResponse.status)
        }
    }

    @Test
    fun `test PUT request success`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"status": "accepted"}"""),
                status = HttpStatusCode.Accepted,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        runBlocking {
            val httpClient = KtorApiClient(mockEngine, "https://example.com")
            val httpResponse = httpClient.post(url = "/", body = "")
            val response: StubResponse = httpResponse.body()
            assertEquals("accepted", response.status)
            assertEquals(HttpStatusCode.Accepted, httpResponse.status)
        }
    }

    @Test
    fun `test DELETE request success`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"status": "deleted"}"""),
                status = HttpStatusCode.Accepted,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        runBlocking {
            val httpClient = KtorApiClient(mockEngine, "https://example.com")
            val httpResponse = httpClient.post(url = "/", body = "")
            val response: StubResponse = httpResponse.body()
            assertEquals("deleted", response.status)
            assertEquals(HttpStatusCode.Accepted, httpResponse.status)
        }
    }
}