package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.UnauthorizedException
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class Router(protected val httpClient: HttpClient) {
    protected suspend fun performGet(endpoint: String, queryString: Map<String, String> = emptyMap()): HttpResponse {
        return httpClient.get(endpoint) {
            url {
                queryString.forEach { key, value -> parameters.append(key, value) }
            }
        }
    }

    protected suspend inline fun <reified T> performPost(endpoint: String, body: T): HttpResponse {
        return httpClient.post(endpoint) {
            contentType(ContentType.Application.Json)
            setBody(body)
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

    protected suspend inline fun <reified T> fetchJsonApiResource(endpoint: String, queryParameters: Map<String, String> = emptyMap()): T {
        return try {
            val rawResponse = performGet(endpoint, queryParameters)
            val resourceConverter = ResourceConverter(T::class.java).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }
            val jsonApiResponse = resourceConverter.readDocument<T>(rawResponse.body<ByteArray>(), T::class.java)

            jsonApiResponse.get() ?: throw ApiException("Failed to parse response for $endpoint", Exception())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorised action: $endpoint", e.response, e)
            }

            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }

    protected suspend inline fun <reified T> fetchJsonApiResources(endpoint: String, queryParameters: Map<String, String> = emptyMap()): List<T> {
        return try {
            val rawResponse = performGet(endpoint, queryParameters)
            val resourceConverter = ResourceConverter(T::class.java).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }
            val jsonApiResponse = resourceConverter.readDocumentCollection<T>(rawResponse.body<ByteArray>(), T::class.java)

            jsonApiResponse.get() ?: throw ApiException("Failed to parse response for $endpoint", Exception())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                throw UnauthorizedException("Unauthorised action: $endpoint", e.response, e)
            }

            throw ApiClientException("Request failed: $endpoint", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Request failed: $endpoint", e)
        }
    }
}