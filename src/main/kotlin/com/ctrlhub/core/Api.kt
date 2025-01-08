package com.ctrlhub.core

import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.auth.AuthRouter
import com.ctrlhub.core.iam.IamRouter

class Api private constructor(apiClient: KtorApiClient) {
    val auth: AuthRouter = AuthRouter(apiClient)
    val iam: IamRouter = IamRouter(apiClient)

    companion object {
        fun create(): Api {
            return Api(KtorApiClient.create(Config.apiBaseUrl))
        }
    }
}