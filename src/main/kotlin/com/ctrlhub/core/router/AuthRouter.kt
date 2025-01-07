package com.ctrlhub.core.router

import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.api.payload.auth.LoginPayload
import com.ctrlhub.core.api.response.auth.AuthFlowResponse
import com.ctrlhub.core.api.response.auth.CompleteResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException

class AuthRouter(apiClient: KtorApiClient) : Router(apiClient = apiClient) {
    suspend fun initiate(): AuthFlowResponse {
        return try {
            return apiClient.get(url = "${Config.authBaseUrl}/self-service/login/api").body()
        } catch (e: ClientRequestException) {
            throw ApiClientException("Failed to initiate auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    suspend fun complete(flowId: String, payload: LoginPayload): CompleteResponse {
        return try {
            apiClient.post(url = "${Config.authBaseUrl}/self-service/login?flow=$flowId", body = payload).body()
        } catch (e: ClientRequestException) {
            throw ApiClientException("Failed to complete auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }
}