package com.ctrlhub.core.settings.timebands

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.settings.timebands.response.TimeBand
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class TimeBandsRouterTest {
    @Test
    fun `can retrieve all time bands`() {
        val jsonFilePath = Paths.get("src/test/resources/settings/time-bands/all-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val router = TimeBandsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = router.all("org-123")
            assertIs<java.util.List<TimeBand>>(response)
            assertEquals(3, response.size)
            assertNotNull(response[0].id)
            assertNotNull(response[0].name)
        }
    }

    @Test
    fun `can retrieve a single time band`() {
        val jsonFilePath = Paths.get("src/test/resources/settings/time-bands/one-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val router = TimeBandsRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val response = router.one("org-123", "b1a1a111-1111-1111-1111-111111111111")
            assertIs<TimeBand>(response)
            assertEquals("b1a1a111-1111-1111-1111-111111111111", response.id)
            assertNotNull(response.name)
        }
    }
}
