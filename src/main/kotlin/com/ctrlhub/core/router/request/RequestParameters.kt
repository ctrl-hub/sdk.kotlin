package com.ctrlhub.core.router.request

abstract class AbstractRequestParameters(
    val offset: Int? = null,
    val limit: Int? = null,
    val filters: List<FilterOption> = emptyList()
) {
    open fun toMap(): Map<String, String> {
        val queryParams = mutableMapOf<String, String>()
        offset?.let { queryParams["offset"] = it.toString() }
        limit?.let { queryParams["limit"] = it.toString() }

        val parts = mutableListOf<String>()

        for (expr in filters) {
            parts += expr.format()
        }

        if (parts.isNotEmpty()) {
            queryParams["filter"] = parts.joinToString(",")
        }

        return queryParams
    }
}

class RequestParameters(
    offset: Int? = null,
    limit: Int? = null,
    filterOptions: List<FilterOption> = emptyList()
) : AbstractRequestParameters(offset, limit, filterOptions)

open class RequestParametersWithIncludes<TIncludes>(
    offset: Int? = null,
    limit: Int? = null,
    filters: List<FilterOption> = emptyList(),
    val includes: List<TIncludes> = emptyList()
) : AbstractRequestParameters(offset, limit, filters) where TIncludes : JsonApiIncludes {

    @Suppress("unused")
    fun withIncludes(vararg includes: TIncludes): RequestParametersWithIncludes<TIncludes> {
        return copy(includes = this.includes + includes)
    }

    override fun toMap(): Map<String, String> {
        val queryParams = super.toMap().toMutableMap()

        if (includes.isNotEmpty()) {
            queryParams["include"] = buildIncludesQueryString(includes)
        }

        return queryParams
    }

    private fun buildIncludesQueryString(includes: List<TIncludes>): String {
        return includes.joinToString(",") { it.value().lowercase() }
    }

    private fun copy(
        includes: List<TIncludes> = this.includes,
        filters: List<FilterOption> = this.filters
    ) = RequestParametersWithIncludes(offset, limit, filters, includes)
}
