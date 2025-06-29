package com.ctrlhub.core.projects.workorders.response

import com.ctrlhub.core.projects.response.Label
import com.ctrlhub.core.projects.operations.response.Operation
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("work-orders")
@JsonIgnoreProperties(ignoreUnknown = true)
class WorkOrder @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("code") var code: String = "",
    @JsonProperty("description") var description: String? = null,
    @JsonProperty("dates") var dates: WorkOrderDates? = null,

    @JsonProperty("labels")
    var labels: List<Label> = emptyList(),

    @Relationship("operations", resolve = true)
    var operations: List<Operation> = emptyList()
) {
    constructor() : this(
        name = "",
        code = "",
        description = "",
        dates = null,
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class WorkOrderDates(
    @JsonProperty("anticipated") val anticipated: WorkOrderAnticipatedDates? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WorkOrderAnticipatedDates(
    @JsonProperty("start") val start: LocalDateTime? = null,
    @JsonProperty("end") val end: LocalDateTime? = null,
)