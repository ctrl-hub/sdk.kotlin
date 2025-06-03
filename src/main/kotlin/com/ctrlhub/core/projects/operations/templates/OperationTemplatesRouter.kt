package com.ctrlhub.core.projects.operations.templates

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.projects.operations.OperationsRouter
import com.ctrlhub.core.projects.operations.templates.response.OperationTemplate
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.HttpClient

class OperationTemplatesRouter(httpClient: HttpClient) : Router(httpClient) {
    suspend fun all(organisationId: String, requestParameters: RequestParameters = RequestParameters()): PaginatedList<OperationTemplate> {
        return fetchPaginatedJsonApiResources("/v3/orgs/$organisationId/projects/operation-templates", requestParameters.toMap())
    }

    suspend fun one(organisationId: String, operationTemplateId: String): OperationTemplate {
        return fetchJsonApiResource("/v3/orgs/$organisationId/projects/operation-templates/$operationTemplateId")
    }
}

val Api.operationTemplates: OperationTemplatesRouter
    get() = OperationTemplatesRouter(httpClient)

val OperationsRouter.templates: OperationTemplatesRouter
    get() = OperationTemplatesRouter(httpClient)