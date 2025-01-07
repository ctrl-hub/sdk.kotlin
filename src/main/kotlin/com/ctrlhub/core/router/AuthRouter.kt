package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.api.payload.auth.LoginPayload
import com.ctrlhub.core.api.response.auth.AuthFlowResponse
import com.ctrlhub.core.api.response.auth.CompleteResponse
import io.ktor.client.call.*

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