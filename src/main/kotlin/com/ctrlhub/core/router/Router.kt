package com.ctrlhub.core.router

import com.ctrlhub.core.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class Router(
    protected val apiClient: ApiClient
) {
    protected suspend fun<T, U> makeNetworkRequest(request: NetworkRequest<T>): Flow<NetworkResult<U>> {
        return flow {
            try {
                val response: NetworkResult<U> = apiClient.handle(request)

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