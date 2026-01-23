package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.UnauthorizedException
import com.ctrlhub.core.api.response.*
import com.ctrlhub.core.json.JsonConfig
import com.ctrlhub.core.router.request.JsonApiRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class Router(val httpClient: HttpClient) {
    protected suspend fun performGet(endpoint: String, queryString: Map<String, String> = emptyMap()): HttpResponse {
        return httpClient.get(endpoint) {
            url {
                queryString.forEach { key, value -> parameters.append(key, value) }
            }
        }
    }

    protected suspend inline fun <reified T> performPost(
        endpoint: String,
        body: T,
        queryParameters: Map<String, String> = emptyMap(),
        contentType: ContentType = ContentType.Application.Json
    ): HttpResponse {
        return httpClient.post(endpoint) {
            contentType(contentType)
            url {
                queryParameters.forEach { key, value -> parameters.append(key, value) }
            }
            setBody(body)
        }
    }

    protected suspend inline fun <reified Res> postJsonApiRequest(
        endpoint: String,
        request: JsonApiRequest<*>,
        queryParameters: Map<String, String> = emptyMap(),
        contentType: ContentType = ContentType.Application.Json,
        vararg includedClasses: Class<*>
    ): Res {
        return try {
            val requestBytes = getObjectMapper().writeValueAsBytes(request)
            val response = httpClient.post(endpoint) {
                contentType(contentType)
                url {
                    queryParameters.forEach { key, value -> parameters.append(key, value) }
                }
                setBody(requestBytes)
            }

            val resourceConverter = ResourceConverter(
                getObjectMapper(),
                Res::class.java,
                *includedClasses
            ).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocument<Res>(
                response.body<ByteArray>(),
                Res::class.java
            )

            jsonApiResponse.get()
                ?: throw ApiException("Failed to parse JSON:API response for $endpoint", Exception())

        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    protected suspend inline fun <reified T> performDelete(endpoint: String, body: T): HttpResponse {
        return httpClient.delete(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    protected suspend inline fun <reified T> performDelete(endpoint: String): HttpResponse {
        return httpClient.delete(endpoint) {
            contentType(ContentType.Application.Json)
        }
    }

    protected suspend inline fun <reified T> fetchJsonApiResource(
        endpoint: String,
        queryParameters: Map<String, String> = emptyMap(),
        vararg includedClasses: Class<*>
    ): T {
        return try {
            val rawResponse = performGet(endpoint, queryParameters)

            val resourceConverter = ResourceConverter(getObjectMapper(), T::class.java, *includedClasses).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocument<T>(
                rawResponse.body<ByteArray>(), T::class.java
            )

            jsonApiResponse.get() ?: throw ApiException("Failed to parse response for $endpoint", Exception())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    protected suspend inline fun <reified T> fetchJsonApiResource(
        response: HttpResponse,
        vararg includedClasses: Class<*>
    ): T {
        return try {
            val resourceConverter = ResourceConverter(getObjectMapper(), T::class.java, *includedClasses).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocument<T>(
                response.body<ByteArray>(), T::class.java
            )

            jsonApiResponse.get() ?: throw ApiException("Failed to parse response", Exception())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action", e.response, e)
            }
            throw ApiClientException("Request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed", e)
        }
    }

    protected suspend inline fun <reified T> fetchJsonApiResources(
        endpoint: String,
        queryParameters: Map<String, String> = emptyMap(),
        vararg includedClasses: Class<*>
    ): List<T> {
        return try {
            val rawResponse = performGet(endpoint, queryParameters)

            val resourceConverter = ResourceConverter(getObjectMapper(), T::class.java, *includedClasses).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
                enableSerializationOption(SerializationFeature.INCLUDE_META)
            }

            val jsonApiResponse = resourceConverter.readDocumentCollection<T>(
                rawResponse.body<ByteArray>(), T::class.java
            )

            val data = jsonApiResponse.meta
            println(data?.size)
            jsonApiResponse.get() ?: throw ApiException("Failed to parse response for $endpoint", Exception())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    fun getObjectMapper(): ObjectMapper {
        return JsonConfig.getMapper()
    }

    protected suspend inline fun <reified T> fetchPaginatedJsonApiResources(
        endpoint: String,
        queryParameters: Map<String, String> = emptyMap(),
        vararg includedClasses: Class<*>
    ): PaginatedList<T> {
        return try {
            val rawResponse = performGet(endpoint, queryParameters)

            val resourceConverter = ResourceConverter(getObjectMapper(), T::class.java, *includedClasses).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
                enableSerializationOption(SerializationFeature.INCLUDE_META)
            }

            val documentCollection = resourceConverter.readDocumentCollection<T>(
                rawResponse.body<ByteArray>(), T::class.java
            )

            buildPaginatedResponse(documentCollection)
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    protected suspend inline fun <reified T> postJsonApiResource(
        endpoint: String,
        requestBody: T,
        queryParameters: Map<String, String> = emptyMap(),
        contentType: ContentType = ContentType.Application.Json,
        vararg includedClasses: Class<*>
    ): T {
        return try {
            val resourceConverter = ResourceConverter(getObjectMapper(), T::class.java, *includedClasses).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiRequestDocument = JSONAPIDocument(requestBody)
            val jsonApiRequest = resourceConverter.writeDocument(jsonApiRequestDocument)

            val response: HttpResponse = performPost(
                endpoint = endpoint,
                body = jsonApiRequest,
                contentType = contentType,
                queryParameters = queryParameters
            )

            val jsonApiResponse = resourceConverter.readDocument<T>(
                response.body<ByteArray>(), T::class.java
            )

            jsonApiResponse.get() ?: throw ApiException("Failed to parse JSON:API response for $endpoint", Exception())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorized action: $endpoint", e.response, e)
            }
            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    fun <T> buildPaginatedResponse(documentCollection: JSONAPIDocument<List<T>>): PaginatedList<T> {
        val metaMap = documentCollection.meta as? Map<*, *> ?: emptyMap<Any, Any>()

        val paginationMap = metaMap["pagination"] as? Map<*, *>
        val countsMap = paginationMap?.get("counts") as? Map<*, *>
        val requestedMap = paginationMap?.get("requested") as? Map<*, *>
        val offsetsMap = paginationMap?.get("offsets") as? Map<*, *>

        val paginationMeta = PaginationMeta(
            page = PageMeta(
                currentPage = (paginationMap?.get("current_page") as? Number)?.toInt() ?: 1
            ),
            counts = CountsMeta(
                resources = (countsMap?.get("resources") as? Number)?.toInt() ?: 0,
                pages = (countsMap?.get("pages") as? Number)?.toInt() ?: 0
            ),
            requested = RequestedMeta(
                offset = (requestedMap?.get("offset") as? Number)?.toInt(),
                limit = (requestedMap?.get("limit") as? Number)?.toInt()
            ),
            offsets = OffsetsMeta(
                previous = (offsetsMap?.get("previous") as? Number)?.toInt(),
                next = (offsetsMap?.get("next") as? Number)?.toInt()
            )
        )

        val data: List<T> = documentCollection.get()?.toList() ?: emptyList()

        return PaginatedList(
            data = data,
            pagination = paginationMeta
        )
    }
}