package com.ctrlhub.core.api

import kotlinx.serialization.Serializable

@Serializable
data class JsonAPIRelationship(
    val type: String,
    val id: String
)