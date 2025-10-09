package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.datacapture.response.FormSubmission
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.projects.response.Organisation
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient
import com.ctrlhub.core.api.response.PaginatedList

class FormSubmissionsRouter(httpClient: HttpClient) : Router(httpClient) {
    /**
     * Get paginated form-submission resources (hydrated with relationships)
     *
     * @return PaginatedList of FormSubmission
     */
    suspend fun all(organisationId: String, formId: String): PaginatedList<FormSubmission> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/data-capture/forms/$formId/submissions",
            queryParameters = emptyMap<String, String>(),
            FormSubmission::class.java,
            User::class.java,
            Form::class.java,
            Organisation::class.java,
            com.ctrlhub.core.datacapture.resource.FormSubmissionVersion::class.java
        )
    }
}

val Api.formSubmissions
    get() = FormSubmissionsRouter(httpClient = httpClient)