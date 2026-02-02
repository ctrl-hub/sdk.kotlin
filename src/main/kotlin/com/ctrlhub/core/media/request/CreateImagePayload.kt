package com.ctrlhub.core.media.request

import com.ctrlhub.core.api.JsonAPIRelationship
import com.ctrlhub.core.api.JsonAPIRelationshipData
import com.ctrlhub.core.projects.response.Organisation
import kotlinx.serialization.Serializable

@Serializable
data class CreateImagePayload(
    val data: CreateImagePayloadData
)

@Serializable
data class CreateImagePayloadRelationships(
    val organisation: JsonAPIRelationshipData
)

@Serializable
data class CreateImagePayloadData(
    val type: String = "images",
    val attributes: CreateImagePayloadAttributes,
    val relationships: CreateImagePayloadRelationships
)

@Serializable
data class CreateImagePayloadAttributes(
    val content: String
)
