package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.vehicles.resource.VehicleInspection
import com.ctrlhub.core.assets.vehicles.resource.VehicleInspectionChecks
import com.ctrlhub.core.configureForTest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class VehicleInspectionsRouterTest {
    @Test
    fun `can retrieve all vehicle inspections`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/all-vehicle-inspections-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehicleInspectionsRouter = VehicleInspectionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehicleInspectionsRouter.all(
                organisationId = "org-123",
                vehicleId = "vehicle-123"
            )
            assertIs<PaginatedList<VehicleInspection>>(response)
            assertNotNull(response.data[0].id)
            assertNotNull(response.data[0].checks)
        }
    }

    @Test
    fun `can retrieve one vehicle inspection`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/one-vehicle-inspection-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehicleInspectionsRouter = VehicleInspectionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = vehicleInspectionsRouter.one(
                organisationId = "org-123",
                vehicleId = "vehicle-123",
                inspectionId = "inspection-123"
            )
            assertIs<VehicleInspection>(response)
            assertNotNull(response.id)
            assertNotNull(response.checks)
        }
    }

    @Test
    fun `can create a vehicle inspection`() {
        val jsonFilePath = Paths.get("src/test/resources/assets/vehicles/one-vehicle-inspection-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val vehicleInspectionsRouter = VehicleInspectionsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val result = vehicleInspectionsRouter.create(
                organisationId = "org-123",
                vehicleId = "vehicle-123",
                payload = VehicleInspection(
                    checks = VehicleInspectionChecks(
                        accessories = listOf(true, false, null).random(),
                        beacons = listOf(true, false, null).random(),
                        chemicalsAndFuel = listOf(true, false, null).random(),
                        cleanliness = listOf(true, false, null).random(),
                        driverChecks = listOf(true, false, null).random(),
                        engineWarningLights = listOf(true, false, null).random(),
                        levels = listOf(true, false, null).random(),
                        lightsAndIndicators = listOf(true, false, null).random(),
                        numberPlate = listOf(true, false, null).random(),
                        reversingAlarm = listOf(true, false, null).random(),
                        safeAccess = listOf(true, false, null).random(),
                        security = listOf(true, false, null).random(),
                        spareNumberPlate = listOf(true, false, null).random(),
                        storage = listOf(true, false, null).random(),
                        tyres = listOf(true, false, null).random(),
                        visibleDamage = listOf(true, false, null).random(),
                        washersAndWipers = listOf(true, false, null).random(),
                        windscreen = listOf(true, false, null).random()
                    ),
                    inspectedAt = LocalDateTime.now()
                )
            )

            assertNotNull(result.id)
        }
    }
}