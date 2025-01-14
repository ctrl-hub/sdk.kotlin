package com.ctrlhub.core.auth.response

import com.ctrlhub.core.serializer.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AuthFlowResponse(
    val id: String
)

@Serializable
data class SessionResponse(
    val active: Boolean,

    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("expires_at") val expiresAt: LocalDateTime,

    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("authenticated_at") val authenticatedAt: LocalDateTime,
)

@Serializable
data class CompleteResponse(
    @SerialName("session_token") val sessionToken: String,
    val session: SessionResponse
)