package com.ctrlhub.core.api.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthFlowResponse(
    val id: String
)

@Serializable
data class CompleteResponse(
    @SerialName("session_token") val sessionToken: String
)