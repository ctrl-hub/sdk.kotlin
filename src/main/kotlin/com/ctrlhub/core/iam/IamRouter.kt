package com.ctrlhub.core.iam

import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.router.Router
import com.github.jasminb.jsonapi.ResourceConverter
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.headers

class IamRouter(httpClient: HttpClient) : Router(httpClient) {

    suspend fun whoami(sessionToken: String): User {
        return try {
            val rawResponse = httpClient.get("${Config.apiBaseUrl}/v3/iam/whoami") {
                headers {
                    header("X-Session-Token", sessionToken)
                }
            }

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