package com.ctrlhub.core.governance.schemes.workorders.operations

import com.ctrlhub.core.Api
import com.ctrlhub.core.governance.schemes.workorders.WorkOrdersRouter
import com.ctrlhub.core.governance.schemes.workorders.operations.response.Operation
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient

class OperationsRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all operations
     *
     * @param organisationId String The organisation ID to retrieve all operations for
     * @param schemeId String The scheme ID to retrieve all operations for
     * @param workOrderId String The work order ID to retreive all operations for
     *
     * @return A list of all operations
     */
    suspend fun all(organisationId: String, schemeId: String, workOrderId: String): List<Operation> {
        val endpoint = "/v3/orgs/$organisationId/governance/schemes/$schemeId/work-orders/$workOrderId/operations"

        return fetchJsonApiResources(endpoint)
    }

    /**
     * Retrieve a single operation
     *
     * @param organisationId String The organisation ID to retrieve all operations for
     * @param schemeId String The scheme ID to retrieve all operations for
     * @param workOrderId String The work order ID to retrieve all operations for
     * @param operationId String The operation ID to retrieve data for
     *
     * @return A list of all operations
     */
    suspend fun one(organisationId: String, schemeId: String, workOrderId: String, operationId: String): Operation {
        val endpoint = "/v3/orgs/$organisationId/governance/schemes/$schemeId/work-orders/$workOrderId/operations/$operationId"

        return fetchJsonApiResource(endpoint)
    }
}

val Api.operations: OperationsRouter
    get() = OperationsRouter(httpClient)

val WorkOrdersRouter.operations: OperationsRouter
    get() = OperationsRouter(httpClient)