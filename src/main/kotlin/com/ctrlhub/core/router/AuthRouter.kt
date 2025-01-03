package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClient
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        return flow {
            try {
                val response: NetworkResult<AuthFlowResponse> = apiClient.handle(InitiateRequest)

                when (response) {
                    is NetworkResult.Success -> {
                        emit(NetworkResult.Success(response.data))
                    }
                    is NetworkResult.Error -> {
                        emit(NetworkResult.Error(response.message))
                    }
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error("Request failed: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun complete(payload: LoginPayload): Flow<NetworkResult<CompleteResponse>> {
        return flow {
            try {
                val response: NetworkResult<CompleteResponse> = apiClient.handle(CompleteRequest(payload))

                when (response) {
                    is NetworkResult.Success -> {
                        emit(NetworkResult.Success(response.data))
                    }
                    is NetworkResult.Error -> {
                        emit(NetworkResult.Error(response.message))
                    }
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error("Request failed: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)
    }
}