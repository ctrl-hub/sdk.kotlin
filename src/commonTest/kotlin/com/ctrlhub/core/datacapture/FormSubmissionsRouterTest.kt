package com.ctrlhub.core.datacapture

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertIs

class FormSubmissionsRouterTest {

    @Test
    fun `can create form submission`() {
        val jsonFilePath = Paths.get("src/commonTest/resources/datacapture/form-submission-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/vnd.api+json")
            )
        }

        val formSubmissionsRouter = FormSubmissionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = formSubmissionsRouter.create(
                organisationId = "org-123",
                formId = "form-456",
                schemaId = "schema-789",
                payload = mapOf("Test" to "value")
            )

            assertIs<FormSubmissionVersion>(result)
            assertNotNull(result.id)
        }
    }
}