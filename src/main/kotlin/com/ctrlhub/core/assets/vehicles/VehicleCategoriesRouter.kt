package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.assets.vehicles.response.VehicleCategory
import com.ctrlhub.core.router.Router
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

class VehicleCategoriesRouter(httpClient: HttpClient) : Router(httpClient, requiresAuthentication = true) {
    private val endpoint = "${Config.apiBaseUrl}/v3/assets/vehicles/categories"

    suspend fun all(): List<VehicleCategory> {
        return try {
            val rawResponse = performGet(endpoint)
            val resourceConverter = ResourceConverter(VehicleCategory::class.java).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocumentCollection<VehicleCategory>(
                rawResponse.body<ByteArray>(),
                VehicleCategory::class.java
            )

            jsonApiResponse.get()!!
        } catch (e: ClientRequestException) {
            throw ApiClientException("All vehicle categories request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("All vehicle categories request failed", e)
        }
    }
}