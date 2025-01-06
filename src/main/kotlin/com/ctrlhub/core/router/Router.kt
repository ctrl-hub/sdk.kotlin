package com.ctrlhub.core.router

import com.ctrlhub.core.api.KtorApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class Router(
    protected val apiClient: KtorApiClient
)