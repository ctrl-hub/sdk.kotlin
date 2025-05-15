package com.ctrlhub.core.datacapture

import com.ctrlhub.core.configureForTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class FormsRouterTest {

    @Test
    fun `test can get all forms successfully`() {
        // Load mocked JSON response
        val jsonFilePath = Paths.get("src/test/resources/datacapture/all-forms-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        // Create a mock engine to intercept HTTP calls
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val formsRouter = FormsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val organisationId = "test-org-id"
            val response = formsRouter.all(organisationId)

            // Assuming the mock response includes 1 form as in your earlier example
            assertEquals(1, response.size)
            assertEquals("Form 3", response[0].name)
            assertEquals("active", response[0].status)
        }
    }
}