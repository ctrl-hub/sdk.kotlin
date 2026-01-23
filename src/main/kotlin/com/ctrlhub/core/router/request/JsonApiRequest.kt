package com.ctrlhub.core.router.request

data class JsonApiRequest<T>(
    val data: T
)

data class ResourceObject<A, R>(
    val type: String,
    val id: String? = null,
    val attributes: A,
    val relationships: R
)

data class SubmissionAttributes(
    val payload: Map<String, Any?>,
    val iteration: Int
)

data class ToOneRelationship(
    val data: RelationshipData
)

data class RelationshipData(
    val type: String,
    val id: String
)