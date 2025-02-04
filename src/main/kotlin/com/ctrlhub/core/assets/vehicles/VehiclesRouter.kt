package com.ctrlhub.core.assets.vehicles

import com.ctrlhub.core.Api
import com.ctrlhub.core.api.ApiClientException
import com.ctrlhub.core.api.ApiException
import com.ctrlhub.core.assets.vehicles.response.Vehicle
import com.ctrlhub.core.http.KtorClientFactory
import com.ctrlhub.core.router.Router
import com.ctrlhub.core.router.request.JsonApiIncludes
import com.github.jasminb.jsonapi.ResourceConverter
import com.github.jasminb.jsonapi.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*

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

/**
 * A vehicles router that deals with the vehicles realm of the Ctrl Hub API
 */
class VehiclesRouter(httpClient: HttpClient) : Router(httpClient, requiresAuthentication = true) {

    /**
     * Request all vehicles
     *
     * @param organisationId String The organisation ID to retrieve all vehicles for
     * @param includes A variable list of any includes, to return in the response. For example, VehicleIncludes.Specification will provide additional Specification attributes
     *
     * @return A list of all vehicles
     */
    suspend fun all(organisationId: String, vararg includes: VehicleIncludes = emptyArray()): List<Vehicle> {
        return try {
            val includesQuery = buildIncludesQueryString(*includes)
            val rawResponse = performGet("/v3/orgs/$organisationId/assets/vehicles" + (if (includesQuery.isNotEmpty()) "?$includesQuery" else ""))
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