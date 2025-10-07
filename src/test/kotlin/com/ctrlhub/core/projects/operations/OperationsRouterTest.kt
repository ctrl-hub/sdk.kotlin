package com.ctrlhub.core.projects.operations

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.projects.operations.response.Operation
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class OperationsRouterTest {
    @Test
    fun `can retrieve all operations for work order`() {
        val jsonFilePath = Paths.get("src/test/resources/projects/operations/all-operations-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val operationsRouter = OperationsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = operationsRouter.all(
                organisationId = "123",
            )

            assertIs<PaginatedList<Operation>>(response)
            assertNotNull(response.data[0].id)
        }
    }

    @Test
    fun `can retrieve a single operation`() {
        val jsonFilePath = Paths.get("src/test/resources/projects/operations/one-operation-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val operationsRouter = OperationsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = operationsRouter.one(
                organisationId = "123",
                operationId = "ghi"
            )

            assertIs<Operation>(response)
            assertNotNull(response.id)
        }
    }

    @Test
    fun `can retrieve all operations with included templates`() {
        val jsonFilePath = Paths.get("src/test/resources/projects/operations/all-operations-with-included-templates.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val operationsRouter = OperationsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = operationsRouter.all(
                organisationId = "123",
                requestParameters = OperationRequestParameters(
                    includes = listOf(
                        OperationIncludes.Template
                    )
                )
            )

            assertNotNull(response.data[0].template)
            assertEquals("96bc0c11-7f6a-403a-a499-7f9e1bb6811d", response.data[0].template?.id)
            assertEquals("Example Template Name", response.data[0].template?.name)
        }
    }

    @Test
    fun `can retrieve all operations with included appointments`() {
        val jsonFilePath = Paths.get("src/test/resources/projects/operations/all-operations-with-included-appointments.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val operationsRouter = OperationsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = operationsRouter.all(
                organisationId = "123",
                requestParameters = OperationRequestParameters(
                    includes = listOf(
                        OperationIncludes.Appointments
                    )
                )
            )

            assertIs<PaginatedList<Operation>>(response)

            // Find the operation that has an appointment
            val operationWithAppointment = response.data.find { it.appointment != null }
            assertNotNull(operationWithAppointment, "Should have at least one operation with an appointment")

            // Validate the appointment is properly hydrated
            val appointment = operationWithAppointment?.appointment
            assertNotNull(appointment, "Appointment should not be null")
            assertEquals("appointment-1", appointment?.id)
            assertEquals("2025-08-15T07:00:00Z", appointment?.startTime)
            assertEquals("2025-08-15T11:00:00Z", appointment?.endTime)

            // Validate the timeband relationship is hydrated (just the ID)
            assertNotNull(appointment?.timeBand, "TimeBand should not be null")
            assertEquals("timeband-1", appointment?.timeBand?.id)
        }
    }

    @Test
    fun `can retrieve operation with included properties`() {
        val jsonFilePath = Paths.get("src/test/resources/projects/operations/all-operations-with-included-properties.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val operationsRouter = OperationsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = operationsRouter.one(
                organisationId = "123",
                operationId = "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                requestParameters = OperationRequestParameters(
                    includes = listOf(
                        OperationIncludes.Properties
                    )
                )
            )

            assertIs<Operation>(response)
            assertNotNull(response.properties, "Properties should not be null")
            assertEquals(2, response.properties?.size, "Should have 2 properties")

            // Validate first property
            val property1 = response.properties?.find { it.id == "f6a7b8c9-d0e1-4f2a-3b4c-5d6e7f8a9b0c" }
            assertNotNull(property1, "First property should exist")

            // Validate address details
            assertNotNull(property1?.address)
            assertEquals("42, Lorem Street, AB12 3CD", property1?.address?.description)
            assertEquals("42", property1?.address?.number)
            assertEquals("Lorem House", property1?.address?.name)
            assertEquals("Lorem Street", property1?.address?.thoroughfare)
            assertEquals("DOLOR", property1?.address?.postTown)
            assertEquals("AB12 3CD", property1?.address?.postcode)
            assertEquals("United Kingdom", property1?.address?.country)

            // Validate location details
            assertNotNull(property1?.location)
            assertNotNull(property1?.location?.latLong)
            assertEquals(51.5074, property1?.location?.latLong?.latitude)
            assertEquals(-0.1278, property1?.location?.latLong?.longitude)

            // Validate meters
            assertNotNull(property1?.meters)
            assertEquals(1, property1?.meters?.size)
            assertEquals("mprn", property1?.meters?.get(0)?.type)
            assertEquals(1234567890, property1?.meters?.get(0)?.number)

            // Validate UPRN
            assertEquals(100000000001, property1?.uprn)

            // Validate PSR data
            assertNotNull(property1?.psr)
            assertEquals(false, property1?.psr?.indicator)

            // Validate second property with PSR indicator
            val property2 = response.properties?.find { it.id == "e5f6a7b8-c9d0-4e1f-2a3b-4c5d6e7f8a9b" }
            assertNotNull(property2, "Second property should exist")
            assertEquals("15, Dolor Avenue, EF45 6GH", property2?.address?.description)
            assertEquals(100000000002, property2?.uprn)

            // Validate meters for property2
            assertNotNull(property2?.meters)
            assertEquals(1, property2?.meters?.size)
            assertEquals("mprn", property2?.meters?.get(0)?.type)
            assertEquals(9876543210, property2?.meters?.get(0)?.number)

            // Validate PSR
            assertEquals(true, property2?.psr?.indicator)
        }
    }
}