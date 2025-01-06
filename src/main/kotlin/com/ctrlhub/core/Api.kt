package com.ctrlhub.core

import com.ctrlhub.core.api.ApiClient
import com.ctrlhub.core.router.AuthRouter

class Api(val apiClient: ApiClient) {
    val auth: AuthRouter = AuthRouter(apiClient)
}