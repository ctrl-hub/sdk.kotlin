package com.ctrlhub.core.assets.equipment.exposures

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.equipment.exposures.resource.EquipmentExposureResource
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

class EquipmentExposuresRouterTest {
    @Test
    fun `can retrieve all equipment exposures`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/equipment/exposures/all-exposures-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val exposuresRouter = EquipmentExposuresRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = exposuresRouter.all(organisationId = "000", equipmentId = "000")
            assertIs<PaginatedList<EquipmentExposureResource>>(response)
            assertNotNull(response.data[0].id)
        }
    }

    @Test
    fun `can retrieve one equipment exposure`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/equipment/exposures/one-exposure-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val exposuresRouter = EquipmentExposuresRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = exposuresRouter.one(organisationId = "000", equipmentId = "000", exposureId = "000")
            assertIs<EquipmentExposureResource>(response)
            assertNotNull(response.id)
        }
    }
}