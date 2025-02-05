package com.ctrlhub.core.governance

import com.ctrlhub.core.Api
import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.governance.response.Organisation
import com.ctrlhub.core.http.KtorClientFactory
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import com.github.jasminb.jsonapi.ResourceConverter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

/**
 * An organisation router that deals with the organisations realm of the Ctrl Hub API
 */
class OrganisationsRouter(httpClient: HttpClient) : Router(httpClient, requiresAuthentication = true) {
    private val endpoint = "${Config.apiBaseUrl}/v3/orgs"

    suspend fun all(requestParameters: RequestParameters = RequestParameters()): List<Organisation> {
        return try {
            val rawResponse = performGet(endpoint, requestParameters.toMap())
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