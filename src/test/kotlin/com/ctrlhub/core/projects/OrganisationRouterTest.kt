package com.ctrlhub.core.projects

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

class OrganisationRouterTest {

    @Test
    fun `test can get all organisations successfully`() {
        val jsonFilePath = Paths.get("src/test/resources/projects/all-organisations-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val organisationRouter = OrganisationsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = organisationRouter.all()

            assertEquals(3, response.data.size)
            assertEquals("00000000-0000-0000-0000-000000000001", response.data[0].id)
            assertEquals("00000000-0000-0000-0000-000000000002", response.data[1].id)
        }
    }
}