package com.ctrlhub.core.assets.equipment.exposures

import com.ctrlhub.core.api.response.PaginatedList
import com.ctrlhub.core.assets.equipment.EquipmentRouter
import com.ctrlhub.core.assets.equipment.exposures.resource.EquipmentExposureResource
import com.ctrlhub.core.assets.equipment.resource.EquipmentItem
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.FilterOption
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.ctrlhub.core.router.request.RequestParametersWithIncludes
import io.ktor.client.HttpClient

enum class EquipmentExposureIncludes(val value: String) : JsonApiIncludes {
    Equipment("equipment"),
    EquipmentModel("equipment.model"),
    EquipmentModelManufacturer("equipment.model.manufacturer"),
    EquipmentModelCategories("equipment.model.categories");

    override fun value(): String {
        return value
    }
}

class EquipmentExposureRequestParameters(
    offset: Int = 0,
    limit: Int = 100,
    filterOptions: List<FilterOption> = emptyList(),
    includes: List<EquipmentExposureIncludes> = emptyList()
) : RequestParametersWithIncludes<EquipmentExposureIncludes>(offset, limit, filterOptions, includes)

/**
 * An equipment exposure router that deals with the exposures realm of the Ctrl Hub API
 */
class EquipmentExposuresRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Request all equipment exposures
     *
     * @param organisationId String The organisation ID to retrieve all exposure records for
     * @param equipmentId String The equipment ID to retrieve all exposure records for
     * @param requestParameters RequestParameters An instance of EquipmentExposureRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A list of all equipment exposures
     */
    suspend fun all(
        organisationId: String,
        equipmentId: String,
        requestParameters: EquipmentExposureRequestParameters = EquipmentExposureRequestParameters()
    ): PaginatedList<EquipmentExposureResource> {
        return fetchPaginatedJsonApiResources(
            "/v3/orgs/$organisationId/assets/equipment/$equipmentId/exposures",
            requestParameters.toMap()
        )
    }

    /**
     * Request a single equipment exposures
     *
     * @param organisationId String The organisation ID to retrieve all exposure records for
     * @param equipmentId String The equipment ID to retrieve all exposure records for
     * @param exposureId String The exposure ID record to retrieve
     * @param requestParameters RequestParameters An instance of EquipmentExposureRequestParameters, capturing sorting, filtering and pagination based request params
     *
     * @return A list of all equipment exposures
     */
    suspend fun one(
        organisationId: String,
        equipmentId: String,
        exposureId: String,
        requestParameters: EquipmentExposureRequestParameters = EquipmentExposureRequestParameters()
    ): EquipmentExposureResource {
        return fetchJsonApiResource(
            "/v3/orgs/$organisationId/assets/equipment/$equipmentId/exposures/$exposureId",
            requestParameters.toMap()
        )
    }

    /**
     * Create a new equipment exposure record
     *
     * @param organisationId String The organisation ID to retrieve all exposure records for
     * @param equipmentId String The equipment ID to retrieve all exposure records for
     * @param resource EquipmentExposureResource The resource to create
     *
     * @return A list of all equipment exposures
     */
    suspend fun create(
        organisationId: String,
        equipmentId: String,
        resource: EquipmentExposureResource
    ): EquipmentExposureResource {
        val request = EquipmentExposureRequestParameters()
        return postJsonApiResource(
            "/v3/orgs/$organisationId/assets/equipment/$equipmentId/exposures", resource, request.toMap(),
            EquipmentItem::class.java
        )
    }
}

val EquipmentRouter.exposures: EquipmentExposuresRouter
    get() = EquipmentExposuresRouter(httpClient)
