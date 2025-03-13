package com.ctrlhub.core.governance.schemes.workorders.operations.response

import com.ctrlhub.core.iam.response.User
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDate

@Type("operations")
@JsonIgnoreProperties(ignoreUnknown = true)
class Operation @JsonCreator constructor(
    @JsonProperty("name") var name: String = "",
    @JsonProperty("code") var code: String = "",
    @JsonProperty("description") var description: String = "",
    @JsonProperty("start_date") var startDate: LocalDate? = null,
    @JsonProperty("end_date") var endDate: LocalDate? = null,
    @JsonProperty("uprns") var uprns: List<String> = emptyList(),
    @JsonProperty("usrns") var usrns: List<String> = emptyList(),
    @JsonProperty("completed") var completed: Boolean = false,
    @JsonProperty("aborted") var aborted: Boolean = false,
    @JsonProperty("cancelled") var cancelled: Boolean = false,

    @Relationship("assignees")
    var assignees: List<User> = emptyList()
) {
    constructor(): this(
        name = "",
        code = "",
        description = "",
        startDate = null,
        endDate = null,
        uprns = emptyList(),
        usrns = emptyList(),
        completed = false,
        aborted = false,
        cancelled = false
    )
}