package com.ctrlhub.core.auth

import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.auth.payload.LoginPayload
import com.ctrlhub.core.auth.response.AuthFlowResponse
import com.ctrlhub.core.auth.response.CompleteResponse
import com.ctrlhub.core.router.Router
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.http.*

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

    suspend fun refresh(): AuthFlowResponse {
        return try {
            return apiClient.get(url = "${Config.authBaseUrl}/self-service/login/api?refresh=true").body()
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
            if (e.response.status == HttpStatusCode.BadRequest) {
                val bodyAsString: String = e.response.body()
                if (bodyAsString.contains("credentials are invalid")) {
                    throw InvalidCredentialsException()
                }
            }

            throw ApiClientException("Failed to complete auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }
}