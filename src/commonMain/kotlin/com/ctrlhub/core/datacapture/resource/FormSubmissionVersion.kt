package com.ctrlhub.core.datacapture.resource

import com.ctrlhub.core.datacapture.response.FormSchema
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Relationship
import com.github.jasminb.jsonapi.annotations.Type

@JsonIgnoreProperties(ignoreUnknown = true)
@Type("form-submission-versions")
data class FormSubmissionVersion @JsonCreator constructor(
    @Id(StringIdHandler::class)
    var id: String = "",

    @JsonProperty("payload")
    var payload: Map<String, Any>? = null,

    @JsonProperty("iteration")
    var iteration: Int = 0,

    @Relationship("schema")
    var schema: FormSchema? = null,
)
