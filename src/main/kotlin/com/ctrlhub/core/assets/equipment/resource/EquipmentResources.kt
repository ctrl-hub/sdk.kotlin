package com.ctrlhub.core.assets.equipment.resource

import com.ctrlhub.core.assets.vehicles.resource.Vehicle
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("equipment-items")
@JsonIgnoreProperties(ignoreUnknown = true)
class EquipmentItem @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("serial") var serial: String = "",
    @JsonProperty("status") var status: String? = null,

    @Relationship("model")
    var model: EquipmentModel? = null,

    @Relationship("vehicle")
    var vehicle: Vehicle? = null,

    @Meta
    var meta: EquipmentMeta? = null,
) {
    constructor() : this(
        id = "",
        serial = "",
        status = null
    )
}

class EquipmentMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime? = null,
    @JsonProperty("modified_at") var modifiedAt: LocalDateTime? = null,
    @JsonProperty("exposure_count") var exposureCount: Int = 0
) {
    constructor() : this(
        createdAt = null,
        modifiedAt = null,
        exposureCount = 0
    )
}

@Type("equipment-models")
@JsonIgnoreProperties(ignoreUnknown = true)
class EquipmentModel @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("description") var description: String? = null,
    @JsonProperty("specification") var specification: EquipmentModelSpecification? = null,

    @Relationship("categories")
    var categories: List<EquipmentCategory> = emptyList(),

    @Relationship("manufacturer")
    var manufacturer: EquipmentManufacturer? = null,
) {
    constructor(): this(
        id = "",
        name = "",
        description = null,
        specification = null,
        categories = emptyList(),
        manufacturer = null
    )
}

class EquipmentModelSpecification @JsonCreator constructor(
    @JsonProperty("vibration") var vibration: EquipmentSpecificationVibration? = null
) {
    constructor(): this(
        vibration = null
    )
}

class EquipmentSpecificationVibration @JsonCreator constructor(
    @JsonProperty("magnitude") var magnitude: Double = 0.0
) {
    constructor(): this(
        magnitude = 0.0
    )
}

@Type("equipment-categories")
@JsonIgnoreProperties(ignoreUnknown = true)
class EquipmentCategory @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = ""
) {
    constructor() : this(
        id = "",
        name = ""
    )
}

@Type("equipment-manufacturers")
@JsonIgnoreProperties(ignoreUnknown = true)
class EquipmentManufacturer @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = ""
) {
    constructor(): this(
        id = "",
        name = ""
    )
}
