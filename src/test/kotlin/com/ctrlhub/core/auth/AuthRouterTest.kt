package com.ctrlhub.core.auth

import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.auth.payload.LoginPayload
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthRouterTest {

    @Test
    fun `test initiate auth success`() {
        val mockEngine = MockEngine.Companion { request ->
            respond(
                content = ByteReadChannel("""{"id": "test-123"}"""),
                status = HttpStatusCode.Companion.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(
            apiClient = KtorApiClient.Companion.create(HttpClient(mockEngine))
        )

        runBlocking {
            val response = authRouter.initiate()
            assertEquals("test-123", response.id)
        }
    }

    @Test
    fun `test exception thrown when auth initiate fails`() {
        val mockEngine = MockEngine.Companion { request ->
            respond(
                content = ByteReadChannel("""{}"""),
                status = HttpStatusCode.Companion.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(
            apiClient = KtorApiClient.Companion.create(HttpClient(mockEngine))
        )

        runBlocking {
            assertFailsWith<ApiException> {
                authRouter.initiate()
            }
        }
    }

    @Test
    fun `test can complete auth successfully`() {
        val mockEngine = MockEngine.Companion { request ->
            respond(
                content = ByteReadChannel("""{"session_token": "ses-123"}"""),
                status = HttpStatusCode.Companion.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(
            apiClient = KtorApiClient.Companion.create(HttpClient(mockEngine))
        )

        runBlocking {
            val response = authRouter.complete(
                "test-123", LoginPayload(
                    identifier = "test@example.com",
                    password = "password",
                    method = "password"
                )
            )

            assertEquals("ses-123", response.sessionToken)
        }
    }
}