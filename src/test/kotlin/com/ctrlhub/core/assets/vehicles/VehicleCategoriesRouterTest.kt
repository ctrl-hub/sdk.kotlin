package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.assets.vehicles.response.VehicleCategory
import com.ctrlhub.core.configureForTest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class VehicleCategoriesRouterTest {
    @Test
    fun `can retrieve all vehicle categories`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/all-vehicle-categories-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehicleCategoriesRouter = VehicleCategoriesRouter(httpClient = HttpClient(mockEngine).configureForTest())
        vehicleCategoriesRouter.sessionToken = "sess-123"

        runBlocking {
            val response = vehicleCategoriesRouter.all()
            assertIs<List<VehicleCategory>>(response)
            assertNotNull(response[0].id)
        }
    }
}