package com.ctrlhub.core.projects.appointments.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import com.ctrlhub.core.settings.timebands.response.TimeBand

@Type("appointments")
@JsonIgnoreProperties(ignoreUnknown = true)
class Appointment @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("animals") val animals: Boolean = false,
    @JsonProperty("end_time") val endTime: String = "",
    @JsonProperty("medical_dependency") val medicalDependency: Boolean = false,
    @JsonProperty("notes") val notes: String = "",
    @JsonProperty("on_ecr") val onEcr: Boolean = false,
    @JsonProperty("on_ecr_notes") val onEcrNotes: String = "",
    @JsonProperty("start_time") val startTime: String = "",
    @Relationship("time_band")
    var timeBand: TimeBand? = null
) {
    constructor() : this(
        id = "",
        animals = false,
        endTime = "",
        medicalDependency = false,
        notes = "",
        onEcr = false,
        onEcrNotes = "",
        startTime = "",
        timeBand = null
    )
}