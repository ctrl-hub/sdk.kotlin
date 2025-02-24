package com.ctrlhub.core.assets.vehicles.response

import com.ctrlhub.core.api.Assignable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type

@Type("vehicles")
class Vehicle {
    @Id(StringIdHandler::class)
    var id: String = ""
    var colour: String? = null
    var description: String? = null
    var registration: String = ""
    var status: String = ""
    var vin: String? = null

    @Relationship("specification")
    var specification: VehicleSpecification? = null

    @Relationship("assignee")
    var assignee: Assignable? = null
}

@Type("vehicle-categories")
class VehicleCategory {
    @Id(StringIdHandler::class)
    var id: String = ""
    var name: String = ""
}

@Type("vehicle-specifications")
@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleSpecification {
    @Id(StringIdHandler::class)
    var id: String = ""
    var emissions: Double = 0.0
    var transmission: String = ""
    var year: Int = 0
    @JsonProperty("fuel_type") var fuelType: String = ""
    @JsonProperty("engine_capacity") var engineCapacity: String = ""
    var documentation: List<VehicleDocumentation> = emptyList()

    @Relationship("model")
    var model: VehicleModel? = null
}

@Type("vehicle-manufacturers")
class VehicleManufacturer {
    @Id(StringIdHandler::class)
    var id: String = ""
    var name: String = ""
}

@Type("vehicle-models")
class VehicleModel {
    @Id(StringIdHandler::class)
    var id: String = ""
    var name: String = ""

    @Relationship("manufacturer")
    var manufacturer: VehicleManufacturer? = null
}

class VehicleDocumentation {
    var name: String = ""
    var description: String = ""
    var link: String = ""
}