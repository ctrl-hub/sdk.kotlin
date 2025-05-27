package com.ctrlhub.core.media.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Type

@Type("images")
data class Image @JsonCreator constructor(
    @Id(StringIdHandler::class) val id: String = "",
    @JsonProperty("mime_type") val mimeType: String,
    @JsonProperty("extension") val extension: String,
    @JsonProperty("width") val width: Int,
    @JsonProperty("height") val height: Int,
    @JsonProperty("bytes") val bytes: Long,
    @JsonProperty("dimensions") val dimensions: List<ImageDimensions> = emptyList()
)

data class ImageDimensions(
    val width: Int,
    val height: Int,
)