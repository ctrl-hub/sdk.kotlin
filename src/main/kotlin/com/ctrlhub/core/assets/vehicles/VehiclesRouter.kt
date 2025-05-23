package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.vehicles.resource.Vehicle
import com.ctrlhub.core.iam.response.User
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.*

/**
 * Represents JSONAPI includes that are associated for vehicles
 */
enum class VehicleIncludes(val value: String) : JsonApiIncludes {
    Status("status"),
    Assignee("assignee"),
    Equipment("equipment"),
    EquipmentManufacturer("equipment.manufacturer"),
    EquipmentModel("equipment.model"),
    EquipmentModelCategories("equipment.model.categories"),
    Specification("specification"),
    SpecificationModel("specification.model"),
    SpecificationModelManufacturer("specification.model.manufacturer");

    override fun value(): String {
        return value
    }
}

class VehicleRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filterOptions: List<FilterOption> = emptyList(),
    includes: List<VehicleIncludes> = emptyList()
) : RequestParametersWithIncludes<VehicleIncludes>(offset, limit, filterOptions, includes)

/**
 * A vehicles router that deals with the vehicles realm of the Ctrl Hub API
 */
class VehiclesRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Request all vehicles
     *
     * @param organisationId String The organisation ID to retrieve all vehicles for
     * @param requestParameters RequestParameters An instance of VehicleRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A list of all vehicles
     */
    suspend fun all(
        organisationId: String,
        requestParameters: VehicleRequestParameters = VehicleRequestParameters()
    ): PaginatedList<Vehicle> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/assets/vehicles",
            requestParameters.toMap(),
            User::class.java
        )
    }

    suspend fun one(
        organisationId: String,
        vehicleId: String,
        requestParameters: VehicleRequestParameters = VehicleRequestParameters()
    ): Vehicle {
        return fetchJsonApiResource(
            "/v3/orgs/$organisationId/assets/vehicles/$vehicleId",
            requestParameters.toMap(),
            User::class.java
        )
    }
}

val Api.vehicles: VehiclesRouter
    get() = VehiclesRouter(httpClient)