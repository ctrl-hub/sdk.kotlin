package com.ctrlhub.core.api.response

data class PaginatedList<T>(
    val data: List<T>,
    val pagination: PaginationMeta
)

data class PaginationMeta(
    val page: PageMeta,
    val counts: CountsMeta,
    val requested: RequestedMeta,
    val offsets: OffsetsMeta
)

data class CountsMeta(
    val resources: Int,
    val pages: Int
)

data class PageMeta(
    val currentPage: Int,
)

data class RequestedMeta(
    val offset: Int?,
    val limit: Int?
)

data class OffsetsMeta(
    val previous: Int?,
    val next: Int?
)
