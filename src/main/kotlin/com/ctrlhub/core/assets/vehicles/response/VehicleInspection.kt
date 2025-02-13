package com.ctrlhub.core.assets.vehicles.response

import com.ctrlhub.core.serializer.JacksonLocalDateTimeSerializer
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("vehicle-inspections")
@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleInspection {
    @Id(StringIdHandler::class)
    var id: String = ""
    var checks: VehicleInspectionChecks? = null

    @JsonProperty("inspected_at")
    @JsonSerialize(contentUsing = JacksonLocalDateTimeSerializer::class)
    var inspectedAt: LocalDateTime? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleInspectionChecks {
    @JsonProperty("visible_damage")
    var visibleDamage: Boolean? = null

    @JsonProperty("tyres")
    var tyres: Boolean? = null

    @JsonProperty("washers_and_wipers")
    var washersAndWipers: Boolean? = null

    @JsonProperty("windscreen")
    var windscreen: Boolean? = null

    @JsonProperty("number_plate")
    var numberPlate: Boolean? = null

    @JsonProperty("security")
    var security: Boolean? = null

    @JsonProperty("accessories")
    var accessories: Boolean? = null

    @JsonProperty("spare_number_plate")
    var spareNumberPlate: Boolean? = null

    @JsonProperty("safe_access")
    var safeAccess: Boolean? = null

    @JsonProperty("reversing_alarm")
    var reversingAlarm: Boolean? = null

    @JsonProperty("beacons")
    var beacons: Boolean? = null

    @JsonProperty("chemicals_and_fuel")
    var chemicalsAndFuel: Boolean? = null

    @JsonProperty("storage")
    var storage: Boolean? = null

    @JsonProperty("lights_and_indicators")
    var lightsAndIndicators: Boolean? = null

    @JsonProperty("engine_warning_lights")
    var engineWarningLights: Boolean? = null

    @JsonProperty("servicing")
    var servicing: Boolean? = null

    @JsonProperty("levels")
    var levels: Boolean? = null

    @JsonProperty("cleanliness")
    var cleanliness: Boolean? = null

    @JsonProperty("driver_checks")
    var driverChecks: Boolean? = null
}