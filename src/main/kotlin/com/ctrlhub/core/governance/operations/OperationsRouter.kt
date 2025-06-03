package com.ctrlhub.core.governance.operations

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.governance.workorders.WorkOrdersRouter
import com.ctrlhub.core.governance.operations.response.Operation
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.AbstractRequestParameters
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.HttpClient

class OperationsRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all operations
     *
     * @param organisationId String The organisation ID to retrieve all operations for
     *
     * @return A list of all operations
     */
    suspend fun all(organisationId: String, requestParameters: RequestParameters = RequestParameters()): PaginatedList<Operation> {
        val endpoint = "/v3/orgs/$organisationId/projects/operations"

        return fetchPaginatedJsonApiResources(endpoint, emptyMap(), Operation::class.java, User::class.java)
    }

    /**
     * Retrieve a single operation
     *
     * @param organisationId String The organisation ID to retrieve all operations for
     * @param operationId String The operation ID to retrieve data for
     *
     * @return A list of all operations
     */
    suspend fun one(organisationId: String, operationId: String): Operation {
        val endpoint = "/v3/orgs/$organisationId/projects/operations/$operationId"

        return fetchJsonApiResource(endpoint, emptyMap(), Operation::class.java, User::class.java)
    }
}

val Api.operations: OperationsRouter
    get() = OperationsRouter(httpClient)

val WorkOrdersRouter.operations: OperationsRouter
    get() = OperationsRouter(httpClient)