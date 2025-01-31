package com.ctrlhub.core.auth.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val identifier: String,
    val password: String,
    val method: String = "password"
)

@Serializable
data class LogoutPayload(
    @SerialName("session_token") val sessionToken: String
)