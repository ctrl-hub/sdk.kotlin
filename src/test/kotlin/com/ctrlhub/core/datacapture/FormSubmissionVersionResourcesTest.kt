package com.ctrlhub.core.datacapture

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.projects.operations.response.Operation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class FormSubmissionVersionResourcesTest {

    @Test
    fun `can auto-hydrate resources`() {
        val jsonFilePath = Paths.get("src/test/resources/datacapture/one-form-submission-version-with-resources.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/vnd.api+json")
            )
        }

        val router = FormSubmissionVersionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        // resource type mappings are registered centrally by configureForTest() / Api

        runBlocking {
            val result = router.one(
                versionId = "a1f9b6c2-3d5a-4a9e-9c1b-0f2e7a4d6b1c"
            )

            assertIs<FormSubmissionVersion>(result)
            assertNotNull(result.id)

            // hydrate image resource
            val image = result.hydrateResourceById("c2d3e4f5-2a34-4b8c-e39d-0e1f2a3b4c5d", Image::class.java)
            assertNotNull(image)
            assertEquals("image/jpeg", image!!.mimeType)
            assertEquals(4000, image.width)
            assertEquals(3000, image.height)
            assertEquals(2136986L, image.bytes)

            // hydrate operation resource
            val op = result.hydrateResourceById("d2e3f4a5-3b45-4c9d-f4ae-1f2a3b4c5d6e", Operation::class.java)
            assertNotNull(op)
            assertEquals("Task 2", op!!.name)
            assertEquals("TK0002", op.code)
        }
    }
}
