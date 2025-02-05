package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Api
import com.ctrlhub.core.assets.vehicles.response.VehicleManufacturer
import com.ctrlhub.core.assets.vehicles.response.VehicleModel
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.*

enum class VehicleManufacturerIncludes(val value: String) : JsonApiIncludes {
    Categories("categories");

    override fun value(): String {
        return value
    }
}

/**
 * A vehicles manufacturer router that deals with the vehicle manufacturers realm of the Ctrl Hub API
 */
class VehicleManufacturersRouter(httpClient: HttpClient) : Router(httpClient) {
    private val endpoint: String = "/v3/assets/vehicles/manufacturers"

    /**
     * Retrieve all vehicle manufacturers
     *
     * @param requestParameters RequestParameters An instance of RequestParameters, capturing sorting, and filtering based request params
     *
     * @return A list of vehicle manufacturers
     */
    suspend fun all(requestParameters: RequestParameters = RequestParameters()): List<VehicleManufacturer> {
        return fetchJsonApiResources(endpoint, requestParameters.toMap())
    }

    suspend fun models(manufacturerId: String, requestParameters: RequestParameters = RequestParameters()): List<VehicleModel> {
        return fetchJsonApiResources("$endpoint/$manufacturerId/models", requestParameters.toMap())
    }
}

val Api.vehicleManufacturers: VehicleManufacturersRouter
    get() = VehicleManufacturersRouter(httpClient)