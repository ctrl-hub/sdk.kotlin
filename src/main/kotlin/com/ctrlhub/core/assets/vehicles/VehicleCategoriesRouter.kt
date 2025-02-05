package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Api
import com.ctrlhub.core.assets.vehicles.response.VehicleCategory
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.*

class VehicleCategoriesRouter(httpClient: HttpClient) : Router(httpClient) {
    private val endpoint = "/v3/assets/vehicles/categories"

    /**
     * Retrieve all vehicle categories
     *
     * @param requestParameters RequestParameters An instance of RequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A list of vehicle categories
     */
    suspend fun all(requestParameters: RequestParameters = RequestParameters()): List<VehicleCategory> {
        return fetchJsonApiResources(endpoint, requestParameters.toMap())
    }
}

val Api.vehicleCategories: VehicleCategoriesRouter
    get() = VehicleCategoriesRouter(httpClient)