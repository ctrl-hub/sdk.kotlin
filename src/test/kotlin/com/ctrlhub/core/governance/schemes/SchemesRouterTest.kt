package com.ctrlhub.core.governance.schemes

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.governance.schemes.response.Scheme
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

class SchemesRouterTest {

    @Test
    fun `can retrieve all schemes`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/all-schemes-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val schemesRouter = SchemesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = schemesRouter.all(organisationId = "123", SchemeRequestParameters(
                includes = listOf(
                    SchemeIncludes.WorkOrders,
                    SchemeIncludes.WorkOrdersOperations
                )
            ))

            assertIs<List<Scheme>>(response)
            assertNotNull(response[0].id)
        }
    }

    @Test
    fun `can retrieve one scheme`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/one-scheme-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val schemesRouter = SchemesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = schemesRouter.one(organisationId = "123", schemeId = "123", SchemeRequestParameters(
                includes = listOf(
                    SchemeIncludes.WorkOrders,
                    SchemeIncludes.WorkOrdersOperations
                )
            ))

            assertIs<Scheme>(response)
            assertNotNull(response.id)
        }
    }

    @Test
    fun `can retrieve a scheme with includes`() {
        val jsonFilePath = Paths.get("src/test/resources/governance/schemes/one-scheme-response-with-includes.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val schemesRouter = SchemesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = schemesRouter.one(organisationId = "123", schemeId = "123", SchemeRequestParameters(
                includes = listOf(
                    SchemeIncludes.WorkOrders
                )
            ))

            assertIs<Scheme>(response)
            assertNotNull(response.id)
            assertNotNull(response.workOrders)
            assertNotNull(response.workOrders.get(0).id)
        }
    }
}