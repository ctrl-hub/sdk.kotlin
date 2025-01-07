package com.ctrlhub.core.api.payload.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val identifier: String,
    val password: String,
    val method: String
)