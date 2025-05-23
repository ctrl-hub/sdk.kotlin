package com.ctrlhub.core.datacapture.response

import com.github.jasminb.jsonapi.annotations.Type
import kotlinx.serialization.json.JsonObject

@Type("form-schemas")
data class FormSchema (
    val id: String,
    val modelConfigStr: String,
    val viewsConfigStr: String,

    val modelConfig: JsonObject?,
    val viewsConfig: JsonObject?,
)