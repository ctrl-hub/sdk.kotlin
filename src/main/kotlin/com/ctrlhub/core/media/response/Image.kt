package com.ctrlhub.core.media.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.jasminb.jsonapi.StringIdHandler
import com.github.jasminb.jsonapi.annotations.Id
import com.github.jasminb.jsonapi.annotations.Meta
import com.github.jasminb.jsonapi.annotations.Type

@Type("images")
class Image @JsonCreator constructor(
    @JsonProperty("id") @Id(StringIdHandler::class) var id: String = "",
    @JsonProperty("mime_type") var mimeType: String = "",
    @JsonProperty("extension") var extension: String = "",
    @JsonProperty("width") var width: Int = 0,
    @JsonProperty("height") var height: Int = 0,
    @JsonProperty("bytes") var bytes: Long = 0L,
    @JsonProperty("dimensions") var dimensions: List<ImageDimensions> = emptyList(),

    @Meta var meta: ImageMeta? = null
) {
    constructor(): this(
        id = "",
        mimeType = "",
        extension = "",
        width = 0,
        height = 0,
        bytes = 0L,
        dimensions = emptyList(),
        meta = null
    )
}

class ImageDimensions(
    var width: Int = 0,
    var height: Int = 0,
) {
    constructor(): this(width = 0, height = 0)
}

class ImageLink(
    var url: String = "",
    var width: Int? = null,
    var height: Int? = null
) {
    constructor(): this(url = "", width = null, height = null)
}

class ImageMeta(
    var links: List<ImageLink> = emptyList(),
    @JsonProperty("created_at") var createdAt: String? = null
) {
    constructor(): this(links = emptyList(), createdAt = null)
}
