package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.datacapture.response.Form
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.projects.response.Organisation
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParameters
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.HttpClient

enum class FormIncludes(val value: String) : JsonApiIncludes {
    LatestSchema("latest_schema"),
    Organisation("organisation");

    override fun value(): String {
        return value
    }
}

class FormRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filters: List<FilterOption> = emptyList(),
    includes: List<FormIncludes> = emptyList()
) : RequestParametersWithIncludes<FormIncludes>(
    offset = offset,
    limit = limit,
    filters = filters,
    includes = includes,
)

/**
 * A router that interacts with the forms realm of the Ctrl Hub API
 */
class FormsRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all forms
     *
     * @return A list of all forms
     */
    suspend fun all(
        requestParameters: FormRequestParameters = FormRequestParameters()
    ): PaginatedList<Form> {
        return fetchPaginatedJsonApiResources(
            "/v3/forms",
            requestParameters.toMap(),
            Form::class.java,
            FormSchema::class.java, Organisation::class.java
        )
        )
    }

    /**
     * Fetch a single form by ID
     *
     * @return FOrm
     */
    suspend fun one(formId: String, requestParameters: FormRequestParameters = FormRequestParameters()): Form {
        return fetchJsonApiResource(
            "/v3/forms/$formId", queryParameters = requestParameters.toMap(), Form::class.java,
            FormSchema::class.java, Organisation::class.java
        )
    }
}

val Api.forms: FormsRouter
    get() = FormsRouter(httpClient)