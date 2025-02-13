package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.api.ApiResourcePayload
import com.ctrlhub.core.assets.vehicles.payload.VehicleInspectionPayload
import com.ctrlhub.core.assets.vehicles.response.VehicleInspection
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.RequestParameters
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import kotlin.Result

/**
 * A router that interacts with the vehicle inspections realm of the Ctrl Hub API
 */
class VehicleInspectionsRouter(httpClient: HttpClient) : Router(httpClient) {
    private val resourceType = "vehicle-inspections"

    /**
     * Retrieve all vehicle inspections
     *
     * @param organisationId String The associated organisation ID
     * @param vehicleId String The vehicle ID for which to retrieve inspections for
     * @param requestParameters RequestParameters An instance representing any request parameters for this API call
     *
     * @return A list of all vehicle inspections applicable to this vehicle
     */
    suspend fun all(
        organisationId: String,
        vehicleId: String,
        requestParameters: RequestParameters = RequestParameters()
    ): List<VehicleInspection> {
        val endpoint = "/v3/orgs/$organisationId/assets/vehicles/$vehicleId/inspections"

        return fetchJsonApiResources(endpoint, requestParameters.toMap())
    }

    /**
     * Retrieve a single vehicle inspections
     *
     * @param organisationId String The associated organisation ID
     * @param vehicleId String The vehicle ID for which to retrieve inspections for
     * @param inspectionId String The specific vehicle inspection ID to retrieve data for
     * @param requestParameters RequestParameters An instance representing any request parameters for this API call
     *
     * @return A VehicleInspection instance containing all data for the requested vehicle inspection
     */
    suspend fun one(
        organisationId: String,
        vehicleId: String,
        inspectionId: String,
        requestParameters: RequestParameters = RequestParameters()
    ): VehicleInspection {
        val endpoint = "/v3/orgs/$organisationId/assets/vehicles/$vehicleId/inspections/$inspectionId"

        return fetchJsonApiResource(endpoint, requestParameters.toMap())
    }

    /**
     * Create a single vehicle inspection
     *
     * @param organisationId String The associated organisation ID to create a vehicle inspection for
     * @param vehicleId String The vehicle ID to create a vehicle inspection against
     * @param payload VehicleInspectionPayload The payload to send
     *
     * @return A result representing the outcome of this operation
     */
    suspend fun create(organisationId: String, vehicleId: String, payload: VehicleInspectionPayload): Result<Boolean> {
        return runCatching {
            val endpoint = "/v3/orgs/$organisationId/assets/vehicles/$vehicleId/inspections"

            val response = performPost(
                endpoint, body = ApiResourcePayload(
                    type = resourceType,
                    data = payload
                )
            )
            response.status == HttpStatusCode.Created
        }
    }
}

val VehiclesRouter.inspections: VehicleInspectionsRouter
    get() = VehicleInspectionsRouter(httpClient)