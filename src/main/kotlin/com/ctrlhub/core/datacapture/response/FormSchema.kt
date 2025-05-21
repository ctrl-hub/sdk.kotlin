package com.ctrlhub.core.datacapture.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.github.jasminb.jsonapi.annotations.Type

@Type("")
data class FormSchema @JsonCreator constructor(
    val id: String,

    @JsonProperty("model")
    private val modelNode: JsonNode,

    @JsonProperty("views")
    private val viewsNode: JsonNode?
) {
    val model: String = modelNode.toString()
    val views: String? = viewsNode?.toString()
}