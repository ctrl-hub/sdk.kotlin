package com.ctrlhub.core.api

import com.ctrlhub.core.LoggerInterface
import com.ctrlhub.core.router.NetworkRequest
import com.ctrlhub.core.router.NetworkResult

interface ApiClient {
    suspend fun <T, U> handle(request: NetworkRequest<T>): NetworkResult<U>
}
