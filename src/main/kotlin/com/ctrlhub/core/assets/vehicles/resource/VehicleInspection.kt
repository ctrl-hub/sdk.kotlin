package com.ctrlhub.core.assets.vehicles.resource

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type
import kotlinx.serialization.SerialName
import java.time.LocalDateTime

@Type("vehicle-inspections")
@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleInspection @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("checks") var checks: VehicleInspectionChecks? = null,
    @JsonProperty("comments") var comments: VehicleInspectionComments? = null,
    @SerialName("inspected_at") @JsonProperty("inspected_at") var inspectedAt: LocalDateTime? = null
) {
    constructor() : this(
        id = "",
        checks = null,
        comments = null,
        inspectedAt = null
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleInspectionComments @JsonCreator constructor(
    @JsonProperty("visible_damage") var visibleDamage: String? = null,
    @JsonProperty("tyres") var tyres: String? = null,
    @JsonProperty("washers_and_wipers") var washersAndWipers: String? = null,
    @JsonProperty("windscreen") var windscreen: String? = null,
    @JsonProperty("number_plate") var numberPlate: String? = null,
    @JsonProperty("security") var security: String? = null,
    @JsonProperty("accessories") var accessories: String? = null,
    @JsonProperty("spare_number_plate") var spareNumberPlate: String? = null,
    @JsonProperty("safe_access") var safeAccess: String? = null,
    @JsonProperty("reversing_alarm") var reversingAlarm: String? = null,
    @JsonProperty("beacons") var beacons: String? = null,
    @JsonProperty("chemicals_and_fuel") var chemicalsAndFuel: String? = null,
    @JsonProperty("storage") var storage: String? = null,
    @JsonProperty("lights_and_indicators") var lightsAndIndicators: String? = null,
    @JsonProperty("engine_warning_lights") var engineWarningLights: String? = null,
    @JsonProperty("servicing") var servicing: String? = null,
    @JsonProperty("levels") var levels: String? = null,
    @JsonProperty("cleanliness") var cleanliness: String? = null,
    @JsonProperty("driver_checks") var driverChecks: String? = null
) {
    constructor() : this(
        visibleDamage = null,
        tyres = null,
        washersAndWipers = null,
        windscreen = null,
        numberPlate = null,
        security = null,
        accessories = null,
        spareNumberPlate = null,
        safeAccess = null,
        reversingAlarm = null,
        beacons = null,
        chemicalsAndFuel = null,
        storage = null,
        lightsAndIndicators = null,
        engineWarningLights = null,
        servicing = null,
        levels = null,
        cleanliness = null,
        driverChecks = null
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleInspectionChecks @JsonCreator constructor(
    @JsonProperty("visible_damage") var visibleDamage: Boolean? = null,
    @JsonProperty("tyres") var tyres: Boolean? = null,
    @JsonProperty("washers_and_wipers") var washersAndWipers: Boolean? = null,
    @JsonProperty("windscreen") var windscreen: Boolean? = null,
    @JsonProperty("number_plate") var numberPlate: Boolean? = null,
    @JsonProperty("security") var security: Boolean? = null,
    @JsonProperty("accessories") var accessories: Boolean? = null,
    @JsonProperty("spare_number_plate") var spareNumberPlate: Boolean? = null,
    @JsonProperty("safe_access") var safeAccess: Boolean? = null,
    @JsonProperty("reversing_alarm") var reversingAlarm: Boolean? = null,
    @JsonProperty("beacons") var beacons: Boolean? = null,
    @JsonProperty("chemicals_and_fuel") var chemicalsAndFuel: Boolean? = null,
    @JsonProperty("storage") var storage: Boolean? = null,
    @JsonProperty("lights_and_indicators") var lightsAndIndicators: Boolean? = null,
    @JsonProperty("engine_warning_lights") var engineWarningLights: Boolean? = null,
    @JsonProperty("servicing") var servicing: Boolean? = null,
    @JsonProperty("levels") var levels: Boolean? = null,
    @JsonProperty("cleanliness") var cleanliness: Boolean? = null,
    @JsonProperty("driver_checks") var driverChecks: Boolean? = null
) {
    constructor() : this(
        visibleDamage = null,
        tyres = null,
        washersAndWipers = null,
        windscreen = null,
        numberPlate = null,
        security = null,
        accessories = null,
        spareNumberPlate = null,
        safeAccess = null,
        reversingAlarm = null,
        beacons = null,
        chemicalsAndFuel = null,
        storage = null,
        lightsAndIndicators = null,
        engineWarningLights = null,
        servicing = null,
        levels = null,
        cleanliness = null,
        driverChecks = null
    )
}