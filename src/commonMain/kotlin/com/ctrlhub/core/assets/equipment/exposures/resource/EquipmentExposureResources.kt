package com.ctrlhub.core.assets.equipment.exposures.resource

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("equipment-exposures")
@JsonIgnoreProperties(ignoreUnknown = true)
class EquipmentExposureResource @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("start_time") var startTime: LocalDateTime? = null,
    @JsonProperty("end_time") var endTime: LocalDateTime? = null,
    @JsonProperty("location") var location: EquipmentExposureLocation? = null,
    @JsonProperty("ppe") var ppe: EquipmentExposurePpe? = EquipmentExposurePpe(),
    @Meta var meta: EquipmentExposureMeta = EquipmentExposureMeta()
)

class EquipmentExposurePpe @JsonCreator constructor(
    @JsonProperty("mask") var mask: Boolean?,
    @JsonProperty("ear_defenders") var earDefenders: Boolean?
) {
    constructor(): this(
        mask = null,
        earDefenders = null
    )
}

class EquipmentExposureMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime? = null,
    @JsonProperty("trigger_time") var triggerTime: Int? = null,
    @JsonProperty("havs") var havs: EquipmentExposureHavsExposureValues = EquipmentExposureHavsExposureValues(),
) {
    constructor(): this(
        createdAt = null,
        triggerTime = null,
        havs = EquipmentExposureHavsExposureValues()
    )
}

class EquipmentExposureHavsExposureValues @JsonCreator constructor(
    @JsonProperty var points: Int? = null,
    @JsonProperty var a8: Double? = null
) {
    constructor(): this(
        points = null,
        a8 = null
    )
}

class EquipmentExposureLocation @JsonCreator constructor(
    @JsonProperty("type")
    val type: String = "Point",

    @JsonProperty("coordinates")
    val coordinates: List<Double>
) {
    constructor(): this(
        coordinates = emptyList()
    )
}