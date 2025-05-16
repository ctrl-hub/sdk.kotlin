package com.ctrlhub.core.assets.equipment

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.equipment.resource.EquipmentItem
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.HttpClient

enum class EquipmentIncludes(val value: String) : JsonApiIncludes {
    Model("model"),
    ModelManufacturer("model.manufacturer"),
    ModelCategory("model.categories"),
    Vehicle("vehicle"),
    VehicleEquipment("vehicle.equipment"),
    VehicleSpecification("vehicle.specification"),
    VehicleSpecificationModel("vehicle.specification.model"),
    VehicleSpecificationModelManufacturer("vehicle.specification.model.manufacturer"),
    VehicleSpecificationModelCategories("vehicle.specification.model.categories");

    override fun value(): String {
        return value
    }
}

class EquipmentRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filterOptions: List<FilterOption> = emptyList(),
    includes: List<EquipmentIncludes> = emptyList()
) : RequestParametersWithIncludes<EquipmentIncludes>(offset, limit, filterOptions, includes)

enum class EquipmentSort(val value: String) {
    Name("name");
}

class EquipmentRouter (httpClient: HttpClient) : Router(httpClient) {

    /**
     * Retrieve a list of all equipment items
     *
     * @param organisationId String The organisation ID to retrieve all equipment items for
     * @param requestParameters RequestParameters An instance of EquipmentRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A list of all equipment items
     */
    suspend fun all(
        organisationId: String,
        requestParameters: EquipmentRequestParameters = EquipmentRequestParameters()
    ) : PaginatedList<EquipmentItem> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/assets/equipment",
            requestParameters.toMap()
        )
    }

    /**
     * Retrieve a single equipment item
     *
     * @param organisationId String The organisation ID related to the equipment item
     * @param equipmentId String The equipment ID to retrieve the item for
     * @param requestParameters RequestParameters An instance of EquipmentRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return An equipment record
     */
    suspend fun one(
        organisationId: String,
        equipmentId: String,
        requestParameters: EquipmentRequestParameters = EquipmentRequestParameters()
    ): EquipmentItem {
        return fetchJsonApiResource(
            "/v3/orgs/$organisationId/assets/equipment/$equipmentId",
            requestParameters.toMap()
        )
    }
}

val Api.equipment: EquipmentRouter
    get() = EquipmentRouter(httpClient)