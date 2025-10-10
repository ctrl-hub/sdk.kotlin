package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import com.ctrlhub.core.api.response.PaginatedList

class FormSubmissionVersionsRouter(httpClient: HttpClient) : Router(httpClient) {
    /**
     * Create a new form submission version
     */
    suspend fun create(organisationId: String, formId: String, schemaId: String, payload: Map<String, Any>): FormSubmissionVersion {
        return postJsonApiResource(
            "/v3/orgs/$organisationId/data-capture/forms/$formId/submissions",
            requestBody = FormSubmissionVersion(
                payload = payload,
                id = "",
                schema = FormSchema(
                    id = schemaId,
                    rawSchema = null,
                )
            ),
            queryParameters = emptyMap(),
            contentType = ContentType.parse("application/vnd.api+json"),
            FormSubmissionVersion::class.java,
            FormSchema::class.java
        )
    }

    /**
     * Get all submission versions for a given form (paginated)
     */
    suspend fun all(organisationId: String, formId: String, submissionId: String): PaginatedList<FormSubmissionVersion> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/data-capture/forms/$formId/submissions/$submissionId/versions",
            queryParameters = emptyMap(),
            FormSubmissionVersion::class.java,
            FormSchema::class.java
        )
    }

    /**
     * Get a single submission version
     */
    suspend fun one(organisationId: String, formId: String, submissionId: String, versionId: String): FormSubmissionVersion {
        return fetchJsonApiResource(
            "/v3/orgs/$organisationId/data-capture/forms/$formId/submissions/$submissionId/versions/$versionId",
            queryParameters = emptyMap(),
            FormSubmissionVersion::class.java,
            FormSchema::class.java
        )
    }
}

val Api.formSubmissionVersions
    get() = FormSubmissionVersionsRouter(httpClient = httpClient)
