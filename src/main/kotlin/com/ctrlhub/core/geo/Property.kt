package com.ctrlhub.core.geo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("properties")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Property(
    @Id(StringIdHandler::class) val id: String? = null,
    val address: Address? = null,
    val location: Location? = null,
    val meters: List<Meter>? = null,
    val psr: Psr? = null,
    val uprn: Long? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Address(
    val description: String? = null,
    val department: String? = null,
    val organisation: String? = null,
    val number: String? = null,
    val name: String? = null,
    val thoroughfare: String? = null,
    @JsonProperty("dependent_thoroughfare") val dependentThoroughfare: String? = null,
    @JsonProperty("post_town") val postTown: String? = null,
    val postcode: String? = null,
    @JsonProperty("po_box") val poBox: String? = null,
    val country: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
    @JsonProperty("british_national_grid") val britishNationalGrid: BritishNationalGrid? = null,
    @JsonProperty("lat_long") val latLong: LatLong? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BritishNationalGrid(
    val easting: Int? = null,
    val northing: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LatLong(
    val latitude: Double? = null,
    val longitude: Double? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Psr(
    val indicator: Boolean? = null,
    val priority: String? = null,
    val notes: String? = null,
    val contact: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Meter(
    val type: String? = null,
    val number: Long? = null
)
