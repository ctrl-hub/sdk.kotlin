package com.ctrlhub.core.governance.schemes.workorders.operations

import com.ctrlhub.core.governance.schemes.workorders.operations.response.Operation
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient

class OperationsRouter(httpClient: HttpClient) : Router(httpClient) {
    suspend fun all(organisationId: String, schemeId: String, workOrderId: String): List<Operation> {
        val endpoint = "/v3/orgs/$organisationId/governance/schemes/$schemeId/work-orders/$workOrderId/operations"

        return fetchJsonApiResources(endpoint)
    }

    suspend fun one(organisationId: String, schemeId: String, workOrderId: String, operationId: String): Operation {
        val endpoint = "/v3/orgs/$organisationId/governance/schemes/$schemeId/work-orders/$workOrderId/operations/$operationId"

        return fetchJsonApiResource(endpoint)
    }
}