package com.ctrlhub.core.projects.operations

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.projects.workorders.WorkOrdersRouter
import com.ctrlhub.core.projects.operations.response.Operation
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.projects.operations.templates.response.OperationTemplate
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParameters
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.HttpClient

enum class OperationIncludes(val value: String) : JsonApiIncludes {
    Template("template");

    override fun value(): String {
        return value
    }
}

class OperationRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filterOptions: List<FilterOption> = emptyList(),
    includes: List<OperationIncludes> = emptyList()
) : RequestParametersWithIncludes<OperationIncludes>(offset, limit, filterOptions, includes)

class OperationsRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all operations
     *
     * @param organisationId String The organisation ID to retrieve all operations for
     *
     * @return A list of all operations
     */
    suspend fun all(
        organisationId: String,
        requestParameters: OperationRequestParameters = OperationRequestParameters()
    ): PaginatedList<Operation> {
        val endpoint = "/v3/orgs/$organisationId/projects/operations"

        return fetchPaginatedJsonApiResources(
            endpoint, requestParameters.toMap(), Operation::class.java,
            OperationTemplate::class.java, User::class.java
        )
    }

    /**
     * Retrieve a single operation
     *
     * @param organisationId String The organisation ID to retrieve all operations for
     * @param operationId String The operation ID to retrieve data for
     *
     * @return A list of all operations
     */
    suspend fun one(
        organisationId: String,
        operationId: String,
        requestParameters: OperationRequestParameters = OperationRequestParameters()
    ): Operation {
        val endpoint = "/v3/orgs/$organisationId/projects/operations/$operationId"

        return fetchJsonApiResource(
            endpoint,
            requestParameters.toMap(),
            Operation::class.java,
            OperationTemplate::class.java,
            User::class.java
        )
    }
}

val Api.operations: OperationsRouter
    get() = OperationsRouter(httpClient)

val WorkOrdersRouter.operations: OperationsRouter
    get() = OperationsRouter(httpClient)