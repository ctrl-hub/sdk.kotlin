package com.ctrlhub.core.iam

import com.ctrlhub.core.Api
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.router.Router
import io.ktor.client.*

/**
 * A router that deals with the Iam realm of the Ctrl Hub API
 */
class IamRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Returns information about the currently authenticated user, based on a session token
     *
     * @return A user object providing information about a currently authenticated user
     */
    suspend fun whoami(): User {
        return fetchJsonApiResource("/v3/iam/whoami")
    }
}

val Api.iam: IamRouter
    get() = IamRouter(httpClient)