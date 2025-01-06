package com.ctrlhub.core

import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.router.AuthRouter

class Api(val apiClient: KtorApiClient) {
    val auth: AuthRouter = AuthRouter(apiClient)
}