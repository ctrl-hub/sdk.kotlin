package com.ctrlhub.core.auth

import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.auth.AuthRouter
import com.ctrlhub.core.auth.payload.LoginPayload
import com.ctrlhub.core.configureForTest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

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

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            assertFailsWith<ApiException> {
                authRouter.initiate()
            }
        }
    }

    @Test
    fun `test can complete auth successfully`() {
        val jsonFilePath = Paths.get("src/test/resources/auth/session-success-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

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

    @Test
    fun `test exception thrown when refresh fails`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{}"""),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            assertFailsWith<ApiException> {
                authRouter.refresh()
            }
        }
    }

    @Test
    fun `test exception thrown when refresh succeeds`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"id": "test-123"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = authRouter.refresh()
            assertEquals("test-123", response.id)
        }
    }

    @Test
    fun `test logout returns true on success`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.NoContent,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = authRouter.logout()
            assertTrue(result, "Logout should return true on success")
        }
    }

    @Test
    fun `test logout returns false on error`() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val authRouter = AuthRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = authRouter.logout()
            assertFalse(result, "Logout should return false on success")
        }
    }
}