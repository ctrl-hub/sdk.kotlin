package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.assets.vehicles.response.Vehicle
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

class VehiclesRouterTest {
    @Test
    fun `test retrieve all vehicles`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/all-vehicles-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehiclesRouter = VehiclesRouter(httpClient = HttpClient(mockEngine).configureForTest())
        vehiclesRouter.sessionToken = "sess-123"

        runBlocking {
            val response = vehiclesRouter.all(organisationId = "123")
            assertIs<List<Vehicle>>(response)
            assertNotNull(response[0].id)
        }
    }

    @Test
    fun `test can retrieve all vehicles with includes`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/all-vehicles-response-with-includes.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehiclesRouter = VehiclesRouter(httpClient = HttpClient(mockEngine).configureForTest())
        vehiclesRouter.sessionToken = "sess-123"

        runBlocking {
            val response = vehiclesRouter.all(organisationId = "123", VehicleIncludes.SpecificationModel)
            assertIs<List<Vehicle>>(response)
            val first = response[0]
            assertNotNull(first.specification?.model)
        }
    }
}