package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.HttpClient

/**
 * A router that interacts with the forms realm of the Ctrl Hub API
 */
class FormsRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all forms
     *
     * @param organisationId String The organisation ID to retrieve all forms for
     *
     * @return A list of all forms
     */
    suspend fun all(
        organisationId: String,
        requestParameters: RequestParameters = RequestParameters()
    ): PaginatedList<Form> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/${organisationId}/data-capture/forms",
            requestParameters.toMap(),
            Form::class.java
        )
    }

    /**
     * Fetch a single form by ID
     *
     * @param organisationId String The organisation ID to retrieve a single form for
     *
     * @return FOrm
     */
    suspend fun one(organisationId: String, formId: String): Form {
        return fetchJsonApiResource("/v3/orgs/${organisationId}/data-capture-forms/${formId}")
    }
}

val Api.forms: FormsRouter
    get() = FormsRouter(httpClient)