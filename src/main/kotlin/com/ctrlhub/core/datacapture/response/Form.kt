package com.ctrlhub.core.datacapture.response

import com.ctrlhub.core.projects.response.Organisation
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("forms")
@JsonIgnoreProperties(ignoreUnknown = true)
class Form @JsonCreator constructor(
    @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("name") var name: String = "",
    @JsonProperty("status") var status: String = "",
    @Relationship("organisation")
    var organisation: Organisation? = null,

    @Relationship("latest_schema")
    var latestSchema: FormSchema? = null,
    @Meta
    var meta: FormMeta? = null
) {
    constructor(): this(
        id = "",
        name = "",
        status = ""
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FormMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime,
    @JsonProperty("updated_at") var updatedAt: LocalDateTime
)