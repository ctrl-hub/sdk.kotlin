package com.ctrlhub.core.iam

import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.api.KtorApiClient
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.router.Router
import com.github.jasminb.jsonapi.ResourceConverter
import io.ktor.client.call.*
import io.ktor.client.plugins.*

class IamRouter(apiClient: KtorApiClient) : Router(apiClient) {

    suspend fun whoami(sessionToken: String): User {
        return try {
            val rawResponse = apiClient.get("/v3/iam/whoami", mapOf("X-Session-Token" to sessionToken))
            val resourceConverter = ResourceConverter(User::class.java)
            val jsonApiResponse = resourceConverter.readDocument<User>(rawResponse.body<ByteArray>(), User::class.java)

            jsonApiResponse.get()!!
        } catch (e: ClientRequestException) {
            throw ApiClientException("Whoami request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Whoami request failed", e)
        }
    }
}