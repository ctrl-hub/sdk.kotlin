package com.ctrlhub.core.media.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateImagePayload(
    val type: String = "images",
    val data: CreateImagePayloadData
)

@Serializable
data class CreateImagePayloadData(
    val attributes: CreateImagePayloadAttributes
)

@Serializable
data class CreateImagePayloadAttributes(
    val content: String
)
