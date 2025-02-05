package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.assets.vehicles.response.VehicleManufacturer
import com.ctrlhub.core.assets.vehicles.response.VehicleModel
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParameters
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

enum class VehicleManufacturerIncludes(val value: String) : JsonApiIncludes {
    Categories("categories");

    override fun value(): String {
        return value
    }
}

/**
 * A vehicles manufacturer router that deals with the vehicle manufacturers realm of the Ctrl Hub API
 */
class VehicleManufacturersRouter(httpClient: HttpClient) : Router(httpClient, requiresAuthentication = true) {
    private val endpoint: String = "${Config.apiBaseUrl}/v3/assets/vehicles/manufacturers"

    /**
     * Retrieve all vehicle manufacturers
     *
     * @param requestParameters RequestParameters An instance of RequestParameters, capturing sorting, and filtering based request params
     *
     * @return A list of vehicle manufacturers
     */
    suspend fun all(requestParameters: RequestParameters = RequestParameters()): List<VehicleManufacturer> {
        return try {
            val rawResponse = performGet(endpoint, requestParameters.toMap())
            val resourceConverter = ResourceConverter(VehicleManufacturer::class.java).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocumentCollection<VehicleManufacturer>(
                rawResponse.body<ByteArray>(),
                VehicleManufacturer::class.java
            )

            jsonApiResponse.get()!!
        } catch (e: ClientRequestException) {
            throw ApiClientException("All vehicle manufacturers request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("All vehicle manufacturers request failed", e)
        }
    }

    suspend fun models(manufacturerId: String, requestParameters: RequestParameters = RequestParameters()): List<VehicleModel> {
        return try {
            val rawResponse = performGet("$endpoint/$manufacturerId/models", requestParameters.toMap())
            val resourceConverter = ResourceConverter(VehicleModel::class.java).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocumentCollection<VehicleModel>(
                rawResponse.body<ByteArray>(),
                VehicleModel::class.java
            )

            jsonApiResponse.get()!!
        } catch (e: ClientRequestException) {
            throw ApiClientException("All vehicle models request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("All vehicle models request failed", e)
        }
    }
}