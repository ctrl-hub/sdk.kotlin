package com.ctrlhub.core

import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.router.AuthRouter

class Api private constructor(apiClient: KtorApiClient) {
    val auth: AuthRouter = AuthRouter(apiClient)

    companion object {
        fun create(): Api {
            return Api(KtorApiClient.create(Config.apiBaseUrl))
        }
    }
}