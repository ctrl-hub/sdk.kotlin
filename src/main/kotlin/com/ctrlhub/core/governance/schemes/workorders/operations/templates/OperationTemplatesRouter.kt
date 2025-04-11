package com.ctrlhub.core.governance.schemes.workorders.operations.templates

import com.ctrlhub.core.Api
import com.ctrlhub.core.governance.schemes.workorders.operations.OperationsRouter
import com.ctrlhub.core.governance.schemes.workorders.operations.templates.response.OperationTemplate
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient

class OperationTemplatesRouter(httpClient: HttpClient) : Router(httpClient) {
    suspend fun all(organisationId: String): List<OperationTemplate> {
        return fetchJsonApiResources("/v3/orgs/$organisationId/governance/operation-templates")
    }

    suspend fun one(organisationId: String, operationTemplateId: String): OperationTemplate {
        return fetchJsonApiResource("/v3/orgs/$organisationId/governance/operation-templates/$operationTemplateId")
    }
}

val Api.operationTemplates: OperationTemplatesRouter
    get() = OperationTemplatesRouter(httpClient)

val OperationsRouter.templates: OperationTemplatesRouter
    get() = OperationTemplatesRouter(httpClient)