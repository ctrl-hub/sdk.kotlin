package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import io.ktor.client.call.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val identifier: String,
    val password: String,
    val method: String
)

@Serializable
data class AuthFlowResponse(
    val id: String
)

@Serializable
data class CompleteResponse(
    @SerialName("session_token") val sessionToken: String
)

class AuthRouter(apiClient: KtorApiClient) : Router(apiClient) {
    suspend fun initiate(): AuthFlowResponse {
        return try {
            return apiClient.get(url = "self-service/login/api").body()
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    suspend fun complete(flowId: String, payload: LoginPayload): CompleteResponse {
        return try {
            apiClient.post(url = "self-service/login?flow=$flowId", body = payload).body()
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }
}