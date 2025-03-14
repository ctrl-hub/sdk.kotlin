package com.ctrlhub.core.governance.schemes

import com.ctrlhub.core.Api
import com.ctrlhub.core.governance.schemes.response.Scheme
import com.ctrlhub.core.governance.schemes.workorders.response.WorkOrder
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.HttpClient

enum class SchemeIncludes(val value: String): JsonApiIncludes {
    WorkOrders("work_orders"),
    WorkOrdersOperations("work_orders.operations");

    override fun value(): String {
        return value
    }
}

class SchemeRequestParameters(
    filterOptions: List<FilterOption> = emptyList(),
    includes: List<SchemeIncludes> = emptyList()
) : RequestParametersWithIncludes<SchemeIncludes>(filterOptions, includes)

class SchemesRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all schemes
     *
     * @param organisationId String The organisation ID to retrieve all schemes for
     * @param requestParameters RequestParameters An instance of SchemeRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A list of all schemes
     */
    suspend fun all(organisationId: String, requestParameters: SchemeRequestParameters = SchemeRequestParameters()): List<Scheme> {
        return fetchJsonApiResources("/v3/orgs/$organisationId/governance/schemes", requestParameters.toMap(), Scheme::class.java,
            WorkOrder::class.java)
    }

    /**
     * Retrieve a single scheme item
     *
     * @param organisationId String The organisation ID to retrieve all equipment items for
     * @param schemeId String The scheme ID to retrieve data for
     * @param requestParameters RequestParameters An instance of SchemeRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A single scheme
     */
    suspend fun one(organisationId: String, schemeId: String, requestParameters: SchemeRequestParameters = SchemeRequestParameters()) : Scheme {
        return fetchJsonApiResource("/v3/orgs/$organisationId/governance/schemes/$schemeId", requestParameters.toMap())
    }
}

val Api.schemes: SchemesRouter
    get() = SchemesRouter(httpClient)