package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClient
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

private object InitiateRequest : NetworkRequest<Nothing>(url = "self-service/login/api")
data class CompleteRequest(val payload: LoginPayload) :
    NetworkRequest<LoginPayload>(method = HttpMethod.Post, url = "self-service/login", body = payload)

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

class AuthRouter(apiClient: ApiClient) : Router(apiClient) {
    suspend fun initiate(): Flow<NetworkResult<AuthFlowResponse>> {
        return makeNetworkRequest(InitiateRequest)
    }

    suspend fun complete(payload: LoginPayload): Flow<NetworkResult<CompleteResponse>> {
        return makeNetworkRequest(CompleteRequest(payload))
    }
}