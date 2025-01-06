package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClient
import com.ctrlhub.core.api.ApiException
import io.ktor.client.call.body
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
    val sessionToken: String
)

sealed class AuthRouter(apiClient: ApiClient) : Router(apiClient) {
    suspend fun initiate(): AuthFlowResponse {
        return try {
            return apiClient.get<AuthFlowResponse>(url = "self-service/login/api").body()
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    suspend fun complete(payload: LoginPayload): CompleteResponse {
        return try {
            apiClient.post<CompleteResponse>(url = "self-service/login", body = payload).body()
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }
}