package com.ctrlhub.core.governance.schemes.workorders.operations.response

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

@Type("operations")
@JsonIgnoreProperties(ignoreUnknown = true)
class Operation @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("code") var code: String = "",
    @JsonProperty("description") var description: String? = "",
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("start_date") var startDate: LocalDate? = null,
    @JsonProperty("end_date") var endDate: LocalDate? = null,
    @JsonProperty("uprns") var uprns: List<String>? = emptyList(),
    @JsonProperty("usrns") var usrns: List<String>? = emptyList(),
    @JsonProperty("completed") var completed: Boolean = false,
    @JsonProperty("aborted") var aborted: Boolean = false,
    @JsonProperty("cancelled") var cancelled: Boolean = false,

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
        startDate = null,
        endDate = null,
        uprns = emptyList(),
        usrns = emptyList(),
        completed = false,
        aborted = false,
        cancelled = false,
        labels = emptyList()
    )
}