package com.ctrlhub.core.geo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("properties")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Property(
    @Id(StringIdHandler::class) val id: String? = null
)
