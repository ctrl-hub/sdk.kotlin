package com.ctrlhub.core.governance

import com.ctrlhub.core.Api
import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.governance.response.Organisation
import com.ctrlhub.core.http.KtorClientFactory
import com.ctrlhub.core.router.Router
import com.github.jasminb.jsonapi.ResourceConverter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.headers

/**
 * An organisation router that deals with the organisations realm of the Ctrl Hub API
 */
class OrganisationsRouter(httpClient: HttpClient) : Router(httpClient) {
    suspend fun all(sessionToken: String): List<Organisation> {
        return try {
            val rawResponse = httpClient.get("${Config.apiBaseUrl}/v3/orgs") {
                headers {
                    header("X-Session-Token", sessionToken)
                }
            }

            val resourceConverter = ResourceConverter(Organisation::class.java)
            val jsonApiResponse = resourceConverter.readDocumentCollection<Organisation>(
                (rawResponse.body<ByteArray>()),
                Organisation::class.java
            )

            jsonApiResponse.get()!!
        } catch (e: ClientRequestException) {
            throw ApiClientException("all organisations GET request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("all organisations GET request failed", e)
        }
    }
}

val Api.organisations: OrganisationsRouter
    get() = OrganisationsRouter(KtorClientFactory.create())