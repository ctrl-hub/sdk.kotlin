package com.ctrlhub.core

import com.ctrlhub.core.api.response.CountsMeta
import com.ctrlhub.core.api.response.OffsetsMeta
import com.ctrlhub.core.api.response.PageMeta
import com.ctrlhub.core.api.response.PaginationMeta
import com.ctrlhub.core.api.response.RequestedMeta
import kotlinx.serialization.json.*

fun extractPaginationFromMeta(json: JsonObject): PaginationMeta {
    val pagination = json["meta"]?.jsonObject?.get("pagination")?.jsonObject
        ?: throw IllegalStateException("Missing pagination metadata")

    val currentPage = pagination["current_page"]?.jsonPrimitive?.intOrNull ?: 1

    val countsJson = pagination["counts"]?.jsonObject ?: JsonObject(emptyMap())
    val counts = CountsMeta(
        resources = countsJson["resources"]?.jsonPrimitive?.intOrNull ?: 0,
        pages = countsJson["pages"]?.jsonPrimitive?.intOrNull ?: 1
    )

    val requestedJson = pagination["requested"]?.jsonObject ?: JsonObject(emptyMap())
    val requested = RequestedMeta(
        offset = requestedJson["offset"]?.jsonPrimitive?.intOrNull,
        limit = requestedJson["limit"]?.jsonPrimitive?.intOrNull
    )

    val offsetsJson = pagination["offsets"]?.jsonObject ?: JsonObject(emptyMap())
    val offsets = OffsetsMeta(
        previous = offsetsJson["previous"]?.jsonPrimitive?.intOrNull,
        next = offsetsJson["next"]?.jsonPrimitive?.intOrNull
    )

    return PaginationMeta(
        page = PageMeta(currentPage = currentPage),
        counts = counts,
        requested = requested,
        offsets = offsets
    )
}