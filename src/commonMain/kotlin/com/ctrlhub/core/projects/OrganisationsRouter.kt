package com.ctrlhub.core.projects

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.projects.response.Organisation
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.*
import io.ktor.client.plugins.*

/**
 * An organisation router that deals with the organisations realm of the Ctrl Hub API
 */
class OrganisationsRouter(httpClient: HttpClient) : Router(httpClient) {
    private val endpoint = "/v3/orgs"

    suspend fun all(requestParameters: RequestParameters = RequestParameters()): PaginatedList<Organisation> {
        return try {
            fetchPaginatedJsonApiResources(endpoint, requestParameters.toMap(), Organisation::class.java)
        } catch (e: ClientRequestException) {
            throw ApiClientException("all organisations GET request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("all organisations GET request failed", e)
        }
    }
}

val Api.organisations: OrganisationsRouter
    get() = OrganisationsRouter(httpClient)