package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.UnauthorizedException
import com.ctrlhub.core.serializer.JacksonLocalDateTimeDeserializer
import com.ctrlhub.core.serializer.JacksonLocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.jasminb.jsonapi.JSONAPIDocument
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.LocalDateTime

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
        queryParameters: Map<String, String> = emptyMap()
    ): HttpResponse {
        return httpClient.post(endpoint) {
            contentType(ContentType.Application.Json)
            url {
                queryParameters.forEach { key, value -> parameters.append(key, value) }
            }
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
            }

            val jsonApiResponse = resourceConverter.readDocumentCollection<T>(
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

    fun getObjectMapper(): ObjectMapper {
        val module = SimpleModule().apply {
            addSerializer(LocalDateTime::class.java, JacksonLocalDateTimeSerializer())
            addDeserializer(LocalDateTime::class.java, JacksonLocalDateTimeDeserializer())
        }

        return ObjectMapper().apply {
            registerModule(JavaTimeModule())
            registerModule(module)
            registerModule(kotlinModule())
        }
    }

    protected suspend inline fun <reified T> postJsonApiResource(
        endpoint: String,
        requestBody: T,
        queryParameters: Map<String, String> = emptyMap(),
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
}