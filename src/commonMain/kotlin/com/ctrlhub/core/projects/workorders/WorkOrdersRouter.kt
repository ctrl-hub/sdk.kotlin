package com.ctrlhub.core.projects.workorders

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.projects.schemes.SchemesRouter
import com.ctrlhub.core.projects.workorders.response.WorkOrder
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient

class WorkOrdersRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all schemes
     *
     * @param organisationId String The organisation ID to retrieve all schemes for
     *
     * @return A list of all work orders
     */
    suspend fun all(organisationId: String): PaginatedList<WorkOrder> {
        return fetchPaginatedJsonApiResources("/v3/orgs/$organisationId/projects/work-orders")
    }

    /**
     * Fetch a single work order by ID
     *
     * @param organisationId String The organisation ID to retrieve a single work order for
     * @param workOrderId String The work order ID to retrieve data for
     *
     * @return WorkOrder
     */
    suspend fun one(organisationId: String, workOrderId: String): WorkOrder {
        return fetchJsonApiResource("/v3/orgs/$organisationId/projects/work-orders/$workOrderId")
    }
}

val Api.workOrders: WorkOrdersRouter
    get() = WorkOrdersRouter(httpClient)

val SchemesRouter.workOrders: WorkOrdersRouter
    get() = WorkOrdersRouter(httpClient)