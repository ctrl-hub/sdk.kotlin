package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient
import io.ktor.http.ContentType

class FormSubmissionsRouter(httpClient: HttpClient) : Router(httpClient) {
    suspend fun create(organisationId: String, formId: String, schemaId: String, payload: Map<String, Any>): FormSubmissionVersion {
        return postJsonApiResource("/v3/orgs/$organisationId/data-capture/forms/$formId/submissions", requestBody = FormSubmissionVersion(
            payload = payload,
            id = "",
            schema = FormSchema(
                id = schemaId,
                rawSchema = null,
            )
        ), queryParameters = emptyMap(), contentType = ContentType.parse("application/vnd.api+json"), FormSubmissionVersion::class.java,
            FormSchema::class.java)
    }
}

val Api.formSubmissions
    get() = FormSubmissionsRouter(httpClient = httpClient)