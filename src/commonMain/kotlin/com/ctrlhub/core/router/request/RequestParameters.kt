package com.ctrlhub.core.router.request

data class FilterOption(
    val field: String,
    val value: String
)

abstract class AbstractRequestParameters(
    val offset: Int? = null,
    val limit: Int? = null,
    val filterOptions: List<FilterOption>
) {
    open fun toMap(): Map<String, String> {
        val queryParams = mutableMapOf<String, String>()
        offset?.let { queryParams["offset"] = it.toString() }
        limit?.let { queryParams["limit"] = it.toString() }

        filterOptions.forEach { queryParams["filter"] = "${it.field}('${it.value}')" }

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
    filterOptions: List<FilterOption> = emptyList(),
    val includes: List<TIncludes> = emptyList()
) : AbstractRequestParameters(offset, limit, filterOptions) where TIncludes : JsonApiIncludes {

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
        filterOptions: List<FilterOption> = this.filterOptions,
        includes: List<TIncludes> = this.includes
    ) = RequestParametersWithIncludes(offset, limit, filterOptions, includes)
}
