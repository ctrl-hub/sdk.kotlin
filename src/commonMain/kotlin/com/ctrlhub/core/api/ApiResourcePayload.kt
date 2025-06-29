package com.ctrlhub.core.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResourcePayload<T>(
    val type: String,
    val data: T
)