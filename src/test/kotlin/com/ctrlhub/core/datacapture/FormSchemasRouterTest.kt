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
import kotlin.test.assertTrue

class FormSchemasRouterTest {

    @Test
    fun `test can get all form schemas successfully`() {
        // Load mocked JSON response
        val jsonFilePath = Paths.get("src/test/resources/datacapture/all-form-schemas-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        // Create a mock engine to intercept HTTP calls
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val formSchemasRouter = FormSchemasRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val organisationId = "test-org-id"
            val formId = "test-form-id"

            val response = formSchemasRouter.all(organisationId, formId)

            assertEquals(1, response.data.size)
            val formSchema = response.data[0]
            assertEquals("1.0.0", formSchema.id)
            assertTrue(formSchema.rawSchema.isNotEmpty())
        }
    }
}