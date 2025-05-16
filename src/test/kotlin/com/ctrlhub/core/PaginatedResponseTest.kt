package com.ctrlhub.core

import com.ctrlhub.core.datacapture.FormsRouter
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class PaginatedResponseTest {
    @Test
    fun `can retrieve pagination meta correctly`() {
        val jsonFilePath = Paths.get("src/test/resources/paginated-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val formsRouter = FormsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = formsRouter.all(organisationId = "123")
            assertEquals(1, response.pagination.page.currentPage)
            assertEquals(10, response.pagination.counts.resources)
            assertEquals(5, response.pagination.counts.pages)
            assertEquals(0, response.pagination.requested.offset)
            assertEquals(100, response.pagination.requested.limit)
            assertEquals(2, response.pagination.offsets.previous)
            assertEquals(3, response.pagination.offsets.next)
        }
    }
}