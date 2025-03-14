package com.ctrlhub.core.governance.schemes.workorders

import com.ctrlhub.core.governance.schemes.workorders.response.WorkOrder
import com.ctrlhub.core.configureForTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class WorkOrdersRouterTest {

    @Test
    fun `can retrieve all work orders for a scheme`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/workorders/all-work-orders-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val workOrdersRouter = WorkOrdersRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = workOrdersRouter.all(organisationId = "abc123", schemeId = "e3fbd3a4-4c96-4b97-8033-8a1a3b4c2e4c")

            assertIs<List<WorkOrder>>(response)
            assertNotNull(response[0].id)
        }
    }

    @Test
    fun `can retrieve a single work order by ID`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/workorders/one-work-order-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val workOrdersRouter = WorkOrdersRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = workOrdersRouter.one(
                organisationId = "abc123",
                schemeId = "e3fbd3a4-4c96-4b97-8033-8a1a3b4c2e4c",
                workOrderId = "b2d9c34f-57c4-4b93-8ef6-6342de1e1c3d"
            )

            assertIs<WorkOrder>(response)
            assertNotNull(response.id)
        }
    }
}