package com.ctrlhub.core.assets.vehicles.payload

import com.ctrlhub.core.serializer.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class VehicleInspectionPayload(
    val checks: VehicleInspectionChecksPayload,

    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("inspected_at") val inspectedAt: LocalDateTime
)

@Serializable
data class VehicleInspectionChecksPayload(
    val accessories: Boolean? = null,
    val beacons: Boolean? = null,
    @SerialName("chemicals_and_fuel") val chemicalsAndFuel: Boolean? = null,
    val cleanliness: Boolean? = null,
    @SerialName("driver_checks") val driverChecks: Boolean? = null,
    @SerialName("engine_warning_lights") val engineWarningLights: Boolean? = null,
    val levels: Boolean? = null,
    @SerialName("lights_and_indicators") val lightsAndIndicators: Boolean? = null,
    @SerialName("number_plate") val numberPlate: Boolean? = null,
    @SerialName("reversing_alarm") val reversingAlarm: Boolean? = null,
    @SerialName("safe_access") val safeAccess: Boolean? = null,
    val security: Boolean? = null,
    @SerialName("spare_number_plate") val spareNumberPlate: Boolean? = null,
    val storage: Boolean? = null,
    val tyres: Boolean? = null,
    @SerialName("visible_damage") val visibleDamage: Boolean? = null,
    @SerialName("washers_and_wipers") val washersAndWipers: Boolean? = null,
    val windscreen: Boolean? = null,
)