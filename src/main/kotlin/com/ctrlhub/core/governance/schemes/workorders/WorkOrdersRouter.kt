package com.ctrlhub.core.governance.schemes.workorders

import com.ctrlhub.core.Api
import com.ctrlhub.core.governance.schemes.SchemesRouter
import com.ctrlhub.core.governance.schemes.workorders.response.WorkOrder
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient

class WorkOrdersRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all schemes
     *
     * @param organisationId String The organisation ID to retrieve all schemes for
     * @param schemeId String The scheme ID to fetch all work orders for
     *
     * @return A list of all work orders
     */
    suspend fun all(organisationId: String, schemeId: String): List<WorkOrder> {
        return fetchJsonApiResources("/v3/orgs/$organisationId/governance/schemes/$schemeId/work-orders")
    }

    /**
     * Fetch a single work order by ID
     *
     * @param organisationId String The organisation ID to retrieve a single work order for
     * @param schemeId String The scheme ID to retrieve a single work order for
     * @param workOrderId String The work order ID to retrieve data for
     *
     * @return WorkOrder
     */
    suspend fun one(organisationId: String, schemeId: String, workOrderId: String): WorkOrder {
        return fetchJsonApiResource("/v3/orgs/$organisationId/governance/schemes/$schemeId/work-orders/$workOrderId")
    }
}

val Api.workOrders: WorkOrdersRouter
    get() = WorkOrdersRouter(httpClient)

val SchemesRouter.workOrders: WorkOrdersRouter
    get() = WorkOrdersRouter(httpClient)