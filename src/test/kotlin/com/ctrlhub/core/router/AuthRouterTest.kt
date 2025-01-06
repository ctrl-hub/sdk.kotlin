package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthRouterTest {

    @Test
    fun `test initiate auth success`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"id": "test-123"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(
            apiClient = KtorApiClient(mockEngine, "https://example.com")
        )

        runBlocking {
            val response = authRouter.initiate()
            assertEquals("test-123", response.id)
        }
    }

    @Test
    fun `test exception thrown when auth initiate fails`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{}"""),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(
            apiClient = KtorApiClient(mockEngine, "https://example.com")
        )

        runBlocking {
            assertFailsWith<ApiException> {
                authRouter.initiate()
            }
        }
    }

    @Test
    fun `test can complete auth successfully`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"session_token": "ses-123"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(
            apiClient = KtorApiClient(mockEngine, "https://example.com")
        )

        runBlocking {
            val response = authRouter.complete("test-123", LoginPayload(
                identifier = "test@example.com",
                password = "password",
                method = "password"
            ))

            assertEquals("ses-123", response.sessionToken)
        }
    }
}