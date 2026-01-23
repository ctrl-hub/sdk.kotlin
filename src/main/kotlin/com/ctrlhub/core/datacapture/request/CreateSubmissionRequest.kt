package com.ctrlhub.core.datacapture.request

import com.ctrlhub.core.router.request.JsonApiRequest
import com.ctrlhub.core.router.request.RelationshipData
import com.ctrlhub.core.router.request.ResourceObject
import com.ctrlhub.core.router.request.SubmissionAttributes
import com.ctrlhub.core.router.request.ToOneRelationship

data class SubmissionRelationships(
    val schema: ToOneRelationship,
    val organisation: ToOneRelationship,
)

data class CreateSubmissionRequest(
    val payload: Map<String, Any?>,
    val organisationId: String,
    val schemaId: String,
    val iteration: Int = 0
) {
    fun toJsonApiRequest(): JsonApiRequest<ResourceObject<SubmissionAttributes, SubmissionRelationships>> {
        return JsonApiRequest(
            ResourceObject(
                type = "form-submission-versions",
                attributes = SubmissionAttributes(
                    payload = payload,
                    iteration = iteration
                ),
                relationships = SubmissionRelationships(
                    schema = ToOneRelationship(
                        data = RelationshipData(
                            type = "form-schemas",
                            id = schemaId
                        )
                    ),
                    organisation = ToOneRelationship(
                        data = RelationshipData(
                            type = "organisations",
                            id = organisationId
                        )
                    )
                )
            )
        )
    }
}