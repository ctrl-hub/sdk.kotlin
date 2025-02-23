package com.ctrlhub.core.auth

import com.ctrlhub.core.Api
import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.auth.payload.LoginPayload
import com.ctrlhub.core.auth.payload.LogoutPayload
import com.ctrlhub.core.auth.response.AuthFlowResponse
import com.ctrlhub.core.auth.response.CompleteResponse
import com.ctrlhub.core.router.Router
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * A router that deals with authenticating through the Ctrl Hub API
 */
class AuthRouter(httpClient: HttpClient) : Router(httpClient = httpClient) {

    /**
     * Initiates a flow for authentication. This needs to be called first, before completing
     *
     * @return A response containing a flow ID and other information associated with an auth flow
     *
     * @throws ApiClientException when a client based exception occurs, usually as a result of a non-200 HTTP response code
     * @throws ApiException when another type of exception occurs
     */
    suspend fun initiate(): AuthFlowResponse {
        return try {
            httpClient.get("${Config.authBaseUrl}/self-service/login/api").body()
        } catch (e: ClientRequestException) {
            throw ApiClientException("Failed to initiate auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    /**
     * Provides a mechanism for refreshing a session
     *
     * @throws ApiClientException when a client based exception occurs, usually as a result of a non-200 HTTP response code
     * @throws ApiException when another type of exception occurs
     */
    suspend fun refresh(): AuthFlowResponse {
        return try {
            performGet("${Config.authBaseUrl}/self-service/login/api?refresh=true").body()
        } catch (e: ClientRequestException) {
            throw ApiClientException("Failed to initiate auth", e.response, e)
        } catch (e: Exception) {
            throw ApiException("Failed to initiate auth", e)
        }
    }

    /**
     * Completes an authentication flow. This is the second step of authentication, after calling initiate
     *
     * @param flowId String A flow ID obtainer via the initiate call
     * @param payload LoginPayload A payload that is used to authenticate a user
     *
     * @return A response representing a completed authentication response. This will hold session information
     *
     * @throws ApiClientException when a client based exception occurs, usually as a result of a non-200 HTTP response code
     * @throws ApiException when another type of exception occurs
     */
    suspend fun complete(flowId: String, payload: LoginPayload): CompleteResponse {
        return try {
            performPost("${Config.authBaseUrl}/self-service/login", payload, mapOf(
                "flow" to flowId
            )).body()
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

    /**
     * Invalidates a session
     *
     * @return true if the session could be invalidated successfully, false if not
     * @throws ApiException If an exception occurred whilst attempting to invalidate a session
     */
    suspend fun logout(sessionToken: String): Boolean {
        return try {
            val statusCode = performDelete("${Config.authBaseUrl}/self-service/logout/api", body = LogoutPayload(
                sessionToken = sessionToken.toString()
            )).status

            statusCode == HttpStatusCode.NoContent
        } catch (e: ClientRequestException) {
            false
        } catch (e: Exception) {
            throw ApiException("Failed to complete auth", e)
        }
    }
}

val Api.auth: AuthRouter
    get() = AuthRouter(httpClient)