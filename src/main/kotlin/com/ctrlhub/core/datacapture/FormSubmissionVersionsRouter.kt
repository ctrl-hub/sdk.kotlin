package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.datacapture.resource.FormSubmissionVersion
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.media.response.Image
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParametersWithIncludes

@Suppress("unused")
enum class FormSubmissionVersionIncludes(val key: String) : JsonApiIncludes {
    Author("author"),
    Form("form"),
    Organisation("organisation"),
    Schema("schema"),
    Submission("submission"),
    PayloadImages("payload_images"),
    PayloadOperations("payload_operations"),
    PayloadProperties("payload_properties"),
    PayloadUsers("payload_users"),
    PayloadWorkOrders("payload_work_orders"),
    PayloadSchemes("payload_schemes");

    override fun value(): String = key
}

class FormSubmissionVersionRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filterOptions: List<FilterOption> = emptyList(),
    includes: List<FormSubmissionVersionIncludes> = emptyList()
) : RequestParametersWithIncludes<FormSubmissionVersionIncludes>(
    offset = offset,
    limit = limit,
    filterOptions = filterOptions,
    includes = includes,
)

class FormSubmissionVersionsRouter(httpClient: HttpClient) : Router(httpClient) {
    /**
     * Create a new form submission version.
     *
     * Sends a POST request to create a submission (and implicitly its version) for the given form.
     *
     * @param organisationId the organisation UUID the form belongs to
     * @param formId the form UUID to create a submission for
     * @param schemaId the schema UUID to associate with the created version
     * @param payload arbitrary map representing the submission payload (field id -> value)
     * @return the created and hydrated FormSubmissionVersion instance
     * @throws Exception on network or parsing errors
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
     * Get all submission versions for a given form (paginated).
     *
     * Returns a paginated list of FormSubmissionVersion resources. Supports JSON:API include options
     * to hydrate related resources (author, form, schema, images, etc.) via the requestParameters.
     *
     * @param organisationId the organisation UUID
     * @param formId the form UUID
     * @param submissionId the submission UUID to list versions for
     * @param requestParameters optional paging, filter and include parameters
     * @return PaginatedList containing FormSubmissionVersion items and pagination meta
     */
    suspend fun all(
        organisationId: String,
        formId: String,
        submissionId: String,
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): PaginatedList<FormSubmissionVersion> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/data-capture/forms/$formId/submissions/$submissionId/versions",
            requestParameters.toMap(),
            User::class.java,
            Form::class.java,
            FormSchema::class.java,
            Image::class.java
        )
    }

    /**
     * Get all submission versions for a specific submission (paginated).
     *
     * Overload that lists versions by submission id without a form id in the path.
     *
     * @param organisationId the organisation UUID
     * @param submissionId the submission UUID to list versions for
     * @param requestParameters optional paging, filter and include parameters
     * @return PaginatedList containing FormSubmissionVersion items and pagination meta
     */
    suspend fun all(
        organisationId: String,
        submissionId: String,
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): PaginatedList<FormSubmissionVersion> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/data-capture/submissions/$submissionId/versions",
            requestParameters.toMap(),
            User::class.java,
            Form::class.java,
            FormSchema::class.java,
            Image::class.java
        )
    }

    /**
     * Get all form submission versions across an organisation (paginated).
     *
     * Useful for admin-style listing of all submission versions. Supports includes via requestParameters.
     *
     * @param organisationId the organisation UUID
     * @param requestParameters optional paging, filter and include parameters
     * @return PaginatedList containing FormSubmissionVersion items and pagination meta
     */
    suspend fun all(
        organisationId: String,
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): PaginatedList<FormSubmissionVersion> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/data-capture/form-submission-versions",
            requestParameters.toMap(),
            User::class.java,
            Form::class.java,
            FormSchema::class.java,
            Image::class.java
        )
    }

    /**
     * Get a single submission version.
     *
     * Fetches and hydrates a single FormSubmissionVersion resource by id. Supports JSON:API include
     * parameters via requestParameters to hydrate related resources.
     *
     * @param organisationId the organisation UUID
     * @param formId the form UUID the submission belongs to
     * @param submissionId the submission UUID
     * @param versionId the version UUID to fetch
     * @param requestParameters optional paging, filter and include parameters
     * @return the hydrated FormSubmissionVersion
     */
    suspend fun one(
        organisationId: String,
        formId: String,
        submissionId: String,
        versionId: String,
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): FormSubmissionVersion {
        return fetchJsonApiResource(
            "/v3/orgs/$organisationId/data-capture/forms/$formId/submissions/$submissionId/versions/$versionId",
            requestParameters.toMap(),
            User::class.java,
            Form::class.java,
            FormSchema::class.java,
            Image::class.java
        )
    }

    /**
     * Get a single submission version by submission id (no form in path).
     *
     * Fetches and hydrates a single FormSubmissionVersion resource by id using the endpoint that does
     * not include the form id in the path.
     *
     * @param organisationId the organisation UUID
     * @param submissionId the submission UUID
     * @param versionId the version UUID to fetch
     * @param requestParameters optional paging, filter and include parameters
     * @return the hydrated FormSubmissionVersion
     */
    suspend fun one(
        organisationId: String,
        submissionId: String,
        versionId: String,
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): FormSubmissionVersion {
        return fetchJsonApiResource(
            "/v3/orgs/$organisationId/data-capture/submissions/$submissionId/versions/$versionId",
            requestParameters.toMap(),
            User::class.java,
            Form::class.java,
            FormSchema::class.java,
            Image::class.java
        )
    }
}

val Api.formSubmissionVersions
    get() = FormSubmissionVersionsRouter(httpClient = httpClient)
