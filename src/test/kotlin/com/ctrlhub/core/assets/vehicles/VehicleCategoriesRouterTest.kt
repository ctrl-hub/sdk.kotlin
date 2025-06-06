package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.vehicles.resource.VehicleCategory
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

        runBlocking {
            val response = vehicleCategoriesRouter.all()
            assertIs<PaginatedList<VehicleCategory>>(response)
            assertNotNull(response.data[0].id)
        }
    }
}