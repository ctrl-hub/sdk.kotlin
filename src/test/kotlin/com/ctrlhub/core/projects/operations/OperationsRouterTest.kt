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
}