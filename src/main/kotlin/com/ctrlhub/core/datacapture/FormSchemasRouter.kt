package com.ctrlhub.core.datacapture

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.datacapture.response.FormSchema
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.HttpClient

enum class FormSchemaIncludes(val value: String) : JsonApiIncludes {
    Xsources("x-sources");

    override fun value(): String {
        return value
    }
}

class FormSchemaRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filters: List<FilterOption> = emptyList(),
    includes: List<FormSchemaIncludes> = emptyList()
) : RequestParametersWithIncludes<FormSchemaIncludes>(
    offset = offset,
    limit = limit,
    filters = filters,
    includes = includes,
)

class FormSchemasRouter(httpClient: HttpClient) : Router(httpClient) {

    suspend fun all(
        requestParameters: FormSchemaRequestParameters = FormSchemaRequestParameters()
    ): PaginatedList<FormSchema> {
        val endpoint = "/v3/form-schemas"

        return fetchPaginatedJsonApiResources(endpoint, requestParameters.toMap())
    }

    suspend fun one(
        schemaId: String,
        requestParameters: FormSchemaRequestParameters = FormSchemaRequestParameters()
    ): FormSchema {
        val endpoint = "/v3/form-schemas/$schemaId"

        return fetchJsonApiResource(endpoint, requestParameters.toMap())
    }
}

val Api.formSchemas: FormSchemasRouter
    get() = FormSchemasRouter(httpClient)