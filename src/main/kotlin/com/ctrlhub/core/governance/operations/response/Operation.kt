package com.ctrlhub.core.governance.operations.response

import com.ctrlhub.core.api.Assignable
import com.ctrlhub.core.governance.response.Label
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime

@Type("operations")
@JsonIgnoreProperties(ignoreUnknown = true)
class Operation @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("code") var code: String = "",
    @JsonProperty("description") var description: String? = "",
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("scheduled") var scheduledDates: OperationScheduledDates? = null,

    @JsonProperty("labels")
    var labels: List<Label> = emptyList(),

    @Relationship("assignees")
    var assignees: java.util.List<Assignable>? = null
) {
    constructor(): this(
        name = "",
        code = "",
        description = "",
        type = null,
        scheduledDates = null,
        labels = emptyList()
    )
}

data class OperationScheduledDates(
    val start: LocalDateTime? = null,
    var end: LocalDateTime? = null,
)