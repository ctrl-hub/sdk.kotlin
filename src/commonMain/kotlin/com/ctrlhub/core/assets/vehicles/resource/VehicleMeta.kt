package com.ctrlhub.core.assets.vehicles.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleMeta {
    var mot: VehicleMot? = null
    var tax: VehicleTax? = null
    var sorn: Boolean? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleMot {
    @JsonProperty("is_valid") var isValid: Boolean? = null
    @JsonProperty("records") var recordsCount: Int? = null
    @JsonProperty("last") var last: VehicleMotRecord? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleMotRecord {
    var at: LocalDateTime? = null
    var id: String? = null
}

class VehicleTax {
    @JsonProperty("is_valid") var isValid: Boolean? = null
    var due: LocalDate? = null
}