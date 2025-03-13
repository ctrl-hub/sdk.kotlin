package com.ctrlhub.core.governance.schemes.workorders.response

import com.ctrlhub.core.governance.schemes.workorders.operations.response.Operation
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDate

@Type("work-orders")
@JsonIgnoreProperties(ignoreUnknown = true)
class WorkOrder @JsonCreator constructor(
    @JsonProperty("name") var name: String = "",
    @JsonProperty("code") var code: String = "",
    @JsonProperty("description") var description: String = "",
    @JsonProperty("start_date") var startDate: LocalDate? = null,
    @JsonProperty("end_date") var endDate: LocalDate? = null,

    @Relationship("operations")
    var operations: List<Operation> = emptyList()
) {
    constructor() : this(
        name = "",
        code = "",
        description = "",
        startDate = null,
        endDate = null
    )
}