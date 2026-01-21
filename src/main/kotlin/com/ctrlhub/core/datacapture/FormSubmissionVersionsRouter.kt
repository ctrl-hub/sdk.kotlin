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
import com.ctrlhub.core.datacapture.request.CreateSubmissionRequest
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
    filters: List<FilterOption> = emptyList(),
    includes: List<FormSubmissionVersionIncludes> = emptyList()
) : RequestParametersWithIncludes<FormSubmissionVersionIncludes>(
    offset = offset,
    limit = limit,
    filters = filters,
    includes = includes,
)

class FormSubmissionVersionsRouter(httpClient: HttpClient) : Router(httpClient) {
    /**
     * Create a new form submission version.
     *
     * Sends a POST request to create a submission (and implicitly its version) for the given form.
     *
     * @param schemaId the schema UUID to associate with the created version
     * @param payload arbitrary map representing the submission payload (field id -> value)
     * @return the created and hydrated FormSubmissionVersion instance
     * @throws Exception on network or parsing errors
     */
    suspend fun create(schemaId: String, payload: Map<String, Any>): FormSubmissionVersion {
        return postJsonApiResource(
            "/v3/form-submissions",
            requestBody = FormSubmissionVersion(
                payload = payload,
                id = "",
                schema = FormSchema(
                    id = schemaId,
                )
            ),
            queryParameters = emptyMap(),
            contentType = ContentType.parse("application/vnd.api+json"),
            FormSubmissionVersion::class.java,
            FormSchema::class.java
        )
    }

    /**
     * Create a new form submission version using a JSON:API request payload.
     *
     * Sends a POST request to create a form submission (and its initial version)
     * using a fully-constructed JSON:API request body derived from
     * [CreateSubmissionRequest].
     *
     * @param request the submission creation request containing payload data,
     * schema relationship and optional organisation relationship
     * @return the created and hydrated [FormSubmissionVersion] instance
     * @throws Exception on network, serialization or parsing errors
     */
    suspend fun create(request: CreateSubmissionRequest): FormSubmissionVersion {
        return postJsonApiRequest(
            "/v3/form-submissions",
            request.toJsonApiRequest(),
            queryParameters =emptyMap(),
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
     * @param requestParameters optional paging, filter and include parameters
     * @return PaginatedList containing FormSubmissionVersion items and pagination meta
     */
    suspend fun all(
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): PaginatedList<FormSubmissionVersion> {
        return fetchPaginatedJsonApiResources(
            "/v3/form-submission-versions",
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
     * @param versionId the version UUID to fetch
     * @param requestParameters optional paging, filter and include parameters
     * @return the hydrated FormSubmissionVersion
     */
    suspend fun one(
        versionId: String,
        requestParameters: FormSubmissionVersionRequestParameters = FormSubmissionVersionRequestParameters()
    ): FormSubmissionVersion {
        return fetchJsonApiResource(
            "/v3/form-submission-versions/$versionId",
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
