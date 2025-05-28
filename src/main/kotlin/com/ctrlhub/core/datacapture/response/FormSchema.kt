package com.ctrlhub.core.datacapture.response

import com.github.jasminb.jsonapi.annotations.Type

@Type("form-schemas")
data class FormSchema (
    val id: String,
    val rawSchema: String,
)