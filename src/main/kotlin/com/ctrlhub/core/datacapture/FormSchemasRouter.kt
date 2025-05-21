package com.ctrlhub.core.datacapture

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.HttpClient

/**
 * A router that interacts with the form schemas realm of the Ctrl Hub API
 */
class FormSchemasRouter(httpClient: HttpClient): Router(httpClient) {

    /**
     * Get all form schemas for a given form and organisation
     *
     * @param organisationId String The organisation ID to retrieve all for schemas for
     * @param formId String The form ID to retrieve all schemas for
     *
     * @return A paginated response of all form schemas
     */
    suspend fun all(organisationId: String, formId: String, requestParameters: RequestParameters = RequestParameters()): PaginatedList<FormSchema> {
        val endpoint = "/v3/orgs/${organisationId}/data-capture/forms/{$formId}/schemas"

        return fetchPaginatedJsonApiResources(endpoint, requestParameters.toMap(), FormSchema::class.java)
    }
}