package com.ctrlhub.core.datacapture.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("form-schemas")
data class FormSchema(
    @Id(StringIdHandler::class)
    val id: String? = null,
    @JsonIgnore val rawSchema: String? = null,
    @Meta
    var meta: FormSchemaMeta? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FormSchemaMeta @JsonCreator constructor(
    @JsonProperty("created_at") var createdAt: LocalDateTime,
    @JsonProperty("updated_at") var updatedAt: LocalDateTime?,
    @JsonProperty("latest") var latest: FormSchemaLatestMeta? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FormSchemaLatestMeta(
    @JsonProperty("id") val id: String = "",
    @JsonProperty("version") val version: String = "",
)