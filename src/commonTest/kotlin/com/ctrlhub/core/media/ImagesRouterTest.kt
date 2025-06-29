package com.ctrlhub.core.media

import com.ctrlhub.core.configureForTest
import com.ctrlhub.core.media.response.Image
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ImagesRouterTest {

    @Test
    fun `test can get all images successfully`() {
        val jsonFilePath = Paths.get("src/commonTest/resources/media/all-images-response.json")
        val jsonContent = Files.readString(jsonFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val imagesRouter = ImagesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val organisationId = "test-org-id"
            val response = imagesRouter.all(organisationId)

            assertEquals(3, response.data.size)

            val firstImage = response.data.first()
            assertNotNull(firstImage.id)
            assertEquals(".png", firstImage.extension)
            assertEquals(100, firstImage.width)
            assertEquals(56, firstImage.height)
        }
    }

    @Test
    fun `can retrieve one image`() {
        val jsonFilePath = Path("src/commonTest/resources/media/one-image-response.json")
        val jsonContent = jsonFilePath.readText()

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val imagesRouter = ImagesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val organisationId = "123"
            val imageId = "456"
            val response = imagesRouter.one(organisationId = organisationId, imageId = imageId)

            assertIs<Image>(response)
            assertNotNull(response.id)
            assertNotNull(response.mimeType)
        }
    }

    @Test
    fun `can create an image`() {
        val jsonFilePath = Path("src/commonTest/resources/media/create-image-response.json")
        val jsonContent = jsonFilePath.readText()

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(jsonContent),
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val imagesRouter = ImagesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val organisationId = "123"
            val testImageFile = File("src/commonTest/resources/media/sample-image.png")
            val response = imagesRouter.create(organisationId = organisationId, image = testImageFile)

            assertIs<Image>(response)
            assertNotNull(response.id)
            assertNotNull(response.mimeType)
        }
    }

    @Test
    fun `can retrieve image file via proxy`() {
        // Load a small dummy image file as bytes for response simulation
        val imageFilePath = Paths.get("src/commonTest/resources/media/sample-image.png")
        val imageBytes = Files.readAllBytes(imageFilePath)

        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(imageBytes),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "image/png")
            )
        }

        val imagesRouter = ImagesRouter(httpClient = HttpClient(mockEngine).configureForTest())

        runBlocking {
            val organisationId = "123"
            val imageId = "abc123"
            val size = "original.png"

            val resultFile = imagesRouter.proxy(organisationId, imageId, size)

            // Check the file is not empty and has expected size
            assertTrue(resultFile.exists())
            assertEquals(imageBytes.size, resultFile.length().toInt())

            // Optionally verify the file extension matches requested size (".png" here)
            assertTrue(resultFile.name.endsWith(".jpg") || resultFile.name.endsWith(".png")) // adjust if needed
        }
    }
}