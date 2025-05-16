package com.ctrlhub.core.assets.equipment

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.equipment.resource.EquipmentItem
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

class EquipmentRouterTest {
    @Test
    fun `can retrieve all equipment items`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/equipment/all-equipment-response-with-includes.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val equipmentRouter = EquipmentRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = equipmentRouter.all(organisationId = "123", EquipmentRequestParameters(
                includes = listOf(
                    EquipmentIncludes.ModelManufacturer,
                    EquipmentIncludes.ModelCategory
                )
            ))
            assertIs<PaginatedList<EquipmentItem>>(response)
            assertNotNull(response.data[0].id)
        }
    }

    @Test
    fun `can retrieve one equipment item`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/equipment/one-equipment-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val equipmentRouter = EquipmentRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = equipmentRouter.one(organisationId = "123", equipmentId = "123", EquipmentRequestParameters(
                includes = listOf(
                    EquipmentIncludes.ModelManufacturer,
                    EquipmentIncludes.ModelCategory
                )
            ))

            assertIs<EquipmentItem>(response)
            assertNotNull(response.id)
        }
    }

    @Test
    fun `can retrieve an equipment item with includes`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/equipment/one-equipment-response-with-includes.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val equipmentRouter = EquipmentRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = equipmentRouter.one(organisationId = "123", equipmentId = "123", EquipmentRequestParameters(
                includes = listOf(
                    EquipmentIncludes.ModelManufacturer,
                )
            ))

            assertIs<EquipmentItem>(response)
            assertNotNull(response.id)
            assertNotNull(response.model!!.name)
            assertNotNull(response.model!!.manufacturer!!.name)
        }
    }
}