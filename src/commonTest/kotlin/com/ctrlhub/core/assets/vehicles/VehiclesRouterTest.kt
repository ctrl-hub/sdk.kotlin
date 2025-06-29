package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.vehicles.resource.Vehicle
import com.ctrlhub.core.configureForTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class VehiclesRouterTest {
    @Test
    fun `can retrieve all vehicles`() {
        val jsonFilePath = Paths.get("src/commonTest/resources/assets/vehicles/all-vehicles-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehiclesRouter = VehiclesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehiclesRouter.all(organisationId = "123")
            assertIs<PaginatedList<Vehicle>>(response)
            assertNotNull(response.data[0].id)
        }
    }

    @Test
    fun `can retrieve one vehicle`() {
        val jsonFilePath = Paths.get("src/commonTest/resources/assets/vehicles/one-vehicle-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehiclesRouter = VehiclesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehiclesRouter.one(organisationId = "123", vehicleId = "123")
            assertIs<Vehicle>(response)
            assertNotNull(response.id)
        }
    }

    @Test
    fun `can retrieve all vehicles with includes`() {
        val jsonFilePath = Paths.get("src/commonTest/resources/assets/vehicles/all-vehicles-response-with-includes.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehiclesRouter = VehiclesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehiclesRouter.all(organisationId = "123", VehicleRequestParameters(
                includes = listOf(VehicleIncludes.SpecificationModel)
            ))

            assertIs<PaginatedList<Vehicle>>(response)
            val first = response.data[0]
            assertNotNull(first.specification?.model)
        }
    }
}