package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.assets.vehicles.response.Vehicle
import com.ctrlhub.core.assets.vehicles.response.VehicleManufacturer
import com.ctrlhub.core.assets.vehicles.response.VehicleModel
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

class VehiclesManufacturersRouterTest {
    @Test
    fun `can retrieve all vehicles`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/all-vehicle-manufacturers-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehicleManufacturersRouter = VehicleManufacturersRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehicleManufacturersRouter.all()
            assertIs<List<VehicleManufacturer>>(response)
            assertNotNull(response[0].id)
        }
    }

    @Test
    fun `can retrieve all vehicle models for a manufacturer`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/all-vehicle-models-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehicleManufacturersRouter = VehicleManufacturersRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehicleManufacturersRouter.models(manufacturerId = "123")
            assertIs<List<VehicleModel>>(response)
            val first = response[0]
            assertNotNull(first.id)
        }
    }
}