package com.ctrlhub.core.datacapture

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.datacapture.request.CreateSubmissionRequest
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
                schemaId = "schema-789",
                payload = mapOf("Test" to "value")
            )

            assertIs<FormSubmissionVersion>(result)
            assertNotNull(result.id)
        }
    }

    @Test
    fun `can create submission using JsonApiRequest`() {
        // Arrange
        val responseJsonPath =
            Paths.get("src/test/resources/datacapture/form-submission-version-response.json")
        val responseJson = Files.readString(responseJsonPath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = responseJson,
                status = HttpStatusCode.Created,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    "application/vnd.api+json"
                )
            )
        }

        val formSubmissionVersionsRouter = FormSubmissionVersionsRouter(
            httpClient = HttpClient(mockEngine).configureForTest()
        )

        val request = CreateSubmissionRequest(
            schemaId = "89a756e8-97e9-4edb-9c47-f260831c4ab0",
            organisationId = "org-123",
            payload = mapOf(
                "field-1" to "Test value",
                "field-2" to true
            )
        )

        runBlocking {
            val result = formSubmissionVersionsRouter.create(request)
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
            val result = formSubmissionVersionsRouter.all()

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
                versionId = "4a8b7c6d-2e3f-4a1b-9c2d-0e1f2a3b4c5d"
            )

            assertIs<FormSubmissionVersion>(result)
            assertEquals("4a8b7c6d-2e3f-4a1b-9c2d-0e1f2a3b4c5d", result.id)
            // check payload exists and contains expected keys
            assertNotNull(result.payload)
            assertTrue(result.payload!!.containsKey("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            assertTrue(result.payload!!.containsKey("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))

            // check rawPayload is a valid JSON string
            assertNotNull(result.rawPayload)
            assertTrue(result.rawPayload!!.startsWith("{"))
            assertTrue(result.rawPayload!!.endsWith("}"))
            // verify it contains the expected keys in JSON format
            assertTrue(result.rawPayload!!.contains("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            assertTrue(result.rawPayload!!.contains("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
        }
    }

    // New tests for overloads that use submission-only path

    @Test
    fun `can get all form submission versions by submission id and hydrate`() {
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
            val result = formSubmissionVersionsRouter.all()

            assertNotNull(result)
            assertTrue(result.data.isNotEmpty())

            val first = result.data.first()
            assertIs<FormSubmissionVersion>(first)
            assertNotNull(first.id)
        }
    }

    @Test
    fun `can get one form submission version by submission id and hydrate`() {
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
                versionId = "4a8b7c6d-2e3f-4a1b-9c2d-0e1f2a3b4c5d"
            )

            assertIs<FormSubmissionVersion>(result)
            assertEquals("4a8b7c6d-2e3f-4a1b-9c2d-0e1f2a3b4c5d", result.id)
            assertNotNull(result.payload)
            assertTrue(result.payload!!.containsKey("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            assertTrue(result.payload!!.containsKey("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))

            // check rawPayload is a valid JSON string
            assertNotNull(result.rawPayload)
            assertTrue(result.rawPayload!!.startsWith("{"))
            assertTrue(result.rawPayload!!.endsWith("}"))
            // verify it contains the expected keys in JSON format
            assertTrue(result.rawPayload!!.contains("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            assertTrue(result.rawPayload!!.contains("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
        }
    }
}
