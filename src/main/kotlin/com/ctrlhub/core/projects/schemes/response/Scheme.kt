package com.ctrlhub.core.projects.schemes.response

import com.ctrlhub.core.projects.response.Label
import com.ctrlhub.core.projects.workorders.response.WorkOrder
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime

@Type("schemes")
@JsonIgnoreProperties(ignoreUnknown = true)
class Scheme @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String? = "",
    @JsonProperty("code") var code: String? = "",
    @JsonProperty("description") var description: String? = null,
    @JsonProperty("dates") var dates: SchemeDates? = null,

    @JsonProperty("labels")
    var labels: List<Label> = emptyList(),

    @Meta
    var meta: SchemeMeta? = null,

    @Relationship("work_orders", resolve = true)
    var workOrders: List<WorkOrder> = emptyList(),
) {
    constructor(): this(
        id = "",
        name = "",
        description = null,
        code = "",
        dates = null,
        workOrders = emptyList()
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class SchemeDates(
    @JsonProperty("anticipated") var anticipated: SchemeAnticipatedDates? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SchemeAnticipatedDates(
    @JsonProperty("start") var start: LocalDate? = null,
    @JsonProperty("end") var end: LocalDateTime? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
class SchemeMeta @JsonCreator constructor(
    @JsonProperty("completeness") var completeness: SchemeCompleteness? = null,
    @JsonProperty("counts") var counts: SchemeCounts? = null,
)

class SchemeCompleteness @JsonCreator constructor(
    @JsonProperty("counts") var counts: SchemeCompletenessCounts? = null,
    @JsonProperty("percentage") var percentage: SchemePercentages? = null
) {
    constructor(): this(
        counts = null,
        percentage = null
    )
}

class SchemeCompletenessCounts @JsonCreator constructor(
    var aborted: Int = 0,
    var cancelled: Int = 0,
    var completed: Int = 0,
    var unknown: Int = 0
)

class SchemeCounts @JsonCreator constructor(
    var operations: Int = 0,
    var properties: Int = 0,
    var streets: Int = 0,
    @JsonProperty("work_orders") var workOrders: Int = 0
)

class SchemePercentages @JsonCreator constructor(
    var aborted: Int = 0,
    var cancelled: Int = 0,
    var completed: Int = 0,
    var unknown: Int = 0
)