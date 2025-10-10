package com.ctrlhub.core.datacapture

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.api.response.PaginatedList
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FormSubmissionVersionsRouterTest {

    @Test
    fun `can create form submission versions`() {
        val jsonFilePath = Paths.get("src/test/resources/datacapture/form-submission-version-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/vnd.api+json")
            )
        }

        val formSubmissionVersionsRouter = FormSubmissionVersionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = formSubmissionVersionsRouter.create(
                organisationId = "org-123",
                formId = "form-456",
                schemaId = "schema-789",
                payload = mapOf("Test" to "value")
            )

            assertIs<FormSubmissionVersion>(result)
            assertNotNull(result.id)
        }
    }

    @Test
    fun `can get all form submission versions and hydrate`() {
        val jsonFilePath = Paths.get("src/test/resources/datacapture/all-form-submission-versions-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/vnd.api+json")
            )
        }

        val formSubmissionVersionsRouter = FormSubmissionVersionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = formSubmissionVersionsRouter.all(
                organisationId = "org-123",
                formId = "form-456",
                submissionId = "d3c2b1a0-9f8e-7d6c-5b4a-3e2f1d0c9b8a"
            )

            // verify the paginated wrapper
            assertNotNull(result)
            assertTrue(result.data.isNotEmpty())

            // verify hydration of the first item
            val first = result.data.first()
            assertIs<FormSubmissionVersion>(first)
            assertNotNull(first.id)
        }
    }

    @Test
    fun `can get one form submission version and hydrate`() {
        val jsonFilePath = Paths.get("src/test/resources/datacapture/one-form-submission-version-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/vnd.api+json")
            )
        }

        val formSubmissionVersionsRouter = FormSubmissionVersionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = formSubmissionVersionsRouter.one(
                organisationId = "org-123",
                formId = "form-456",
                submissionId = "d4c3b2a1-0f9e-8d7c-6b5a-4e3f2d1c0b9a",
                versionId = "4a8b7c6d-2e3f-4a1b-9c2d-0e1f2a3b4c5d"
            )

            assertIs<FormSubmissionVersion>(result)
            assertEquals("4a8b7c6d-2e3f-4a1b-9c2d-0e1f2a3b4c5d", result.id)
            // check payload exists and contains expected keys
            assertNotNull(result.payload)
            assertTrue(result.payload!!.containsKey("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            assertTrue(result.payload!!.containsKey("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
        }
    }
}
