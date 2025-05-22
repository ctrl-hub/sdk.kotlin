package com.ctrlhub.core.datacapture.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("form-schemas")
data class FormSchema @JsonCreator constructor(
    @Id(StringIdHandler::class) val id: String = "",

    @JsonProperty("model")
    private val modelNode: JsonNode,

    @JsonProperty("views")
    private val viewsNode: JsonNode?
) {
    val model: String = modelNode.toString()
    val views: String? = viewsNode?.toString()
}