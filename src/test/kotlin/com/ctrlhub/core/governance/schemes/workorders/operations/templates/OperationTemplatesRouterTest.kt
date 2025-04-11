package com.ctrlhub.core.governance.schemes.workorders.operations.templates

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.governance.schemes.workorders.operations.templates.response.OperationTemplate
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class OperationTemplatesRouterTest {

    @Test
    fun `can retrieve all operation templates`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/workorders/operations/templates/all-operation-templates-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val router = OperationTemplatesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = router.all(
                organisationId = "org-123"
            )

            assertIs<List<OperationTemplate>>(response)
            assertNotNull(response.first().id)
        }
    }

    @Test
    fun `can retrieve a single operation template`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/workorders/operations/templates/one-operation-template-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val router = OperationTemplatesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = router.one(
                organisationId = "org-123",
                operationTemplateId = "template-456"
            )

            assertIs<OperationTemplate>(response)
            assertNotNull(response.id)
        }
    }
}