package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Api
import com.ctrlhub.core.Config
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.assets.vehicles.response.Vehicle
import com.ctrlhub.core.http.KtorClientFactory
import com.ctrlhub.core.router.Router
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.headers

/**
 * Represents JSONAPI includes that are associated for vehicles
 */
enum class VehicleIncludes(val value: String) {
    Status("status"),
    Assignee("assignee"),
    Equipment("equipment"),
    EquipmentManufacturer("equipment.manufacturer"),
    EquipmentModel("equipment.model"),
    EquipmentModelCategories("equipment.model.categories"),
    Specification("specification"),
    SpecificationModel("specification.model"),
    SpecificationModelManufacturer("specification.model.manufacturer")
}

/**
 * A vehicles router that deals with the vehicles realm of the Ctrl Hub API
 */
class VehiclesRouter(httpClient: HttpClient) : Router(httpClient) {

    /**
     * Request all vehicles
     *
     * @param sessionToken String A valid session token gained after authentication
     * @param organisationId String The organisation ID to retrieve all vehicles for
     * @param includes A variable list of any includes, to return in the response. For example, VehicleIncludes.Specification will provide additional Specification attributes
     *
     * @return A list of all vehicles
     */
    suspend fun all(sessionToken: String, organisationId: String, vararg includes: VehicleIncludes): List<Vehicle> {
        val includesQuery = if (includes.isNotEmpty()) {
            "?include=${includes.joinToString(",") { it.value }}"
        } else {
            ""
        }

        return fetchAllVehicles(sessionToken, organisationId, includesQuery)
    }

    private suspend fun fetchAllVehicles(sessionToken: String, organisationId: String, includesQuery: String): List<Vehicle> {
        return try {
            val rawResponse = httpClient.get("${Config.apiBaseUrl}/v3/orgs/$organisationId/assets/vehicles$includesQuery") {
                headers {
                    header("X-Session-Token", sessionToken)
                }
            }

            val resourceConverter = ResourceConverter(Vehicle::class.java).apply {
                enableSerializationOption(SerializationFeature.INCLUDE_RELATIONSHIP_ATTRIBUTES)
            }

            val jsonApiResponse = resourceConverter.readDocumentCollection<Vehicle>(
                rawResponse.body<ByteArray>(),
                Vehicle::class.java
            )

            jsonApiResponse.get()!!
        } catch (e: ClientRequestException) {
            throw ApiClientException("All vehicles request failed", e.response, e)
        } catch (e: Exception) {
            throw ApiException("All vehicles request failed", e)
        }
    }
}

val Api.vehicles: VehiclesRouter
    get() = VehiclesRouter(KtorClientFactory.create())