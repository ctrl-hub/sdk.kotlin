package com.ctrlhub.core.media

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.JsonAPIRelationship
import com.ctrlhub.core.api.JsonAPIRelationshipData
import com.ctrlhub.core.api.UnauthorizedException
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.media.request.CreateImagePayload
import com.ctrlhub.core.media.request.CreateImagePayloadAttributes
import com.ctrlhub.core.media.request.CreateImagePayloadData
import com.ctrlhub.core.media.request.CreateImagePayloadRelationships
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import java.io.File
import java.net.URLConnection
import java.nio.file.Files
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.createTempFile

/**
 * An images router that deals with the images area of the Ctrl Hub API
 */
class ImagesRouter(httpClient: HttpClient): Router(httpClient) {

    /**
     * Get all image records for a given organisation
     *
     * @param organisationId String The organisation ID to retrieve all image records for
     *
     * @return Paginated response of image records
     */
    suspend fun all(organisationId: String): PaginatedList<Image> {
        return fetchPaginatedJsonApiResources("/v3/images")
    }

    /**
     * Get an image record
     *
     * @param organisationId String The associated organisation ID to retrieve an image record for
     * @param imageId String The image ID to retrieve the record for
     *
     * @return Matching image record
     */
    suspend fun one(organisationId: String, imageId: String): Image {
        return fetchJsonApiResource("/v3/images/$imageId")
    }

    /**
     * Get the actual image data
     *
     * @param organisationId String The associated organisation ID to retrieve an image record for
     * @param imageId String The image ID to retrieve the record for
     * @param size String The size of the image to retrieve, plus the extension. Format of this is either "original.{extension}" or "{width}.{height}.{extension}"
     *
     * @return File instance containing the image data
     */
    suspend fun proxy(organisationId: String, imageId: String, size: String = "original.jpg"): File {
        val endpoint = "/v3/images/$imageId/$size"

        return try {
            val response = performGet(endpoint)
            val bytes = response.body<ByteArray>()

            val tempFile = createTempFile(suffix = ".jpg").toFile()
            tempFile.writeBytes(bytes)

            tempFile
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    /**
     * Creates a new image
     *
     * @param organisationId String The organisation ID to associate this image with
     * @param image File The image file to upload
     *
     * @return Image Image data on successful response
     */
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun create(organisationId: String, image: File): Image {
        val endpoint = "/v3/images"

        return try {
            val bytes = image.readBytes()
            val mimeType = URLConnection.guessContentTypeFromName(image.name)
                ?: "image/png"
            val base64Data = Base64.encode(bytes)
            val dataUri = "data:$mimeType;base64,$base64Data"

            val response = performPost(endpoint, body = CreateImagePayload(
                data = CreateImagePayloadData(
                    attributes = CreateImagePayloadAttributes(
                        content = dataUri
                    ),
                    relationships = CreateImagePayloadRelationships(
                        organisation = JsonAPIRelationshipData(
                            data = JsonAPIRelationship(
                                type = "organisations",
                                id = organisationId
                            )
                        )
                    )
                )
            ), contentType = ContentType.parse("application/vnd.api+json"))

            fetchJsonApiResource(response)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }
}

val Api.images: ImagesRouter
    get() = ImagesRouter(httpClient)