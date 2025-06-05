package com.ctrlhub.core.datacapture.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type
import java.time.LocalDateTime

@Type("form-schemas")
data class FormSchema(
    val id: String? = null,
    val rawSchema: String? = null,
    @Meta
    var meta: FormSchemaMeta? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class FormSchemaMeta(
    @JsonProperty("created_at") var createdAt: LocalDateTime,
    @JsonProperty("updated_at") var updatedAt: LocalDateTime,
    @JsonProperty("latest") var latest: String,
)