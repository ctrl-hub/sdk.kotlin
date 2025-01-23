package com.ctrlhub.core.auth

import com.ctrlhub.core.Api
import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.auth.payload.LoginPayload
import com.ctrlhub.core.auth.payload.LogoutPayload
import com.ctrlhub.core.auth.response.AuthFlowResponse
import com.ctrlhub.core.auth.response.CompleteResponse
import com.ctrlhub.core.http.KtorClientFactory
import com.ctrlhub.core.router.Router
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthRouter(httpClient: HttpClient) : Router(httpClient = httpClient) {
    suspend fun initiate(): AuthFlowResponse {
        return try {
            httpClient.get("${Config.authBaseUrl}/self-service/login/api").body()
        } catch (e: ClientRequestException) {
            throw ApiClientException("Failed to initiate auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    suspend fun refresh(sessionToken: String): AuthFlowResponse {
        return try {
            httpClient.get("${Config.authBaseUrl}/self-service/login/api?refresh=true") {
                headers {
                    header("X-Session-Token", sessionToken)
                }
            }.body()
        } catch (e: ClientRequestException) {
            throw ApiClientException("Failed to initiate auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    suspend fun complete(flowId: String, payload: LoginPayload): CompleteResponse {
        return try {
            httpClient.post("${Config.authBaseUrl}/self-service/login?flow=$flowId") {
                setBody(Json.encodeToString(payload))
            }.body()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.BadRequest) {
                val bodyAsString: String = e.response.body()
                if (bodyAsString.contains("credentials are invalid")) {
                    throw InvalidCredentialsException()
                }
            }

            throw ApiClientException("Failed to complete auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }

    suspend fun logout(sessionToken: String): Boolean {
        return try {
            val statusCode = httpClient.delete("${Config.authBaseUrl}/self-service/logout/api") {
                setBody(Json.encodeToString(LogoutPayload(
                    sessionToken = sessionToken
                )))
            }.status

            statusCode == HttpStatusCode.NoContent
        } catch (e: ClientRequestException) {
            false
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }
}

val Api.auth: AuthRouter
    get() = AuthRouter(KtorClientFactory.create())