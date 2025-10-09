package com.ctrlhub.core.datacapture

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.datacapture.response.FormSubmission
import com.ctrlhub.core.api.response.PaginatedList
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertIs

class FormSubmissionsRouterTest {

    @Test
    fun `can retrieve and hydrate submissions`() {
        val jsonFilePath = Paths.get("src/test/resources/datacapture/all-form-submissions-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/vnd.api+json")
            )
        }

        val formSubmissionsRouter = FormSubmissionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = formSubmissionsRouter.all(organisationId = "org-123", formId = "form-456")

            assertIs<PaginatedList<FormSubmission>>(response)
            assertEquals(4, response.data.size)
            assertNotNull(response.data[0].id)
            // verify pagination counts parsed from meta
            assertEquals(4, response.pagination.counts.resources)
            assertEquals(1, response.pagination.counts.pages)
        }
    }
}