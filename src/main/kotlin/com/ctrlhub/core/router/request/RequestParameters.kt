package com.ctrlhub.core.router.request

data class FilterOption(
    val field: String,
    val value: String
)

abstract class AbstractRequestParameters(
    val filterOptions: List<FilterOption>
) {
    fun withFilters(vararg filters: FilterOption): RequestParameters {
        return RequestParameters(filterOptions = this.filterOptions + filters)
    }

    open fun toMap(): Map<String, String> {
        val queryParams = mutableMapOf<String, String>()

        filterOptions.forEach { queryParams["filter[${it.field}]"] = it.value }

        return queryParams
    }
}

class RequestParameters(
    filterOptions: List<FilterOption> = emptyList()
) : AbstractRequestParameters(filterOptions)

open class RequestParametersWithIncludes<TIncludes>(
    filterOptions: List<FilterOption> = emptyList(),
    val includes: List<TIncludes> = emptyList()
) : AbstractRequestParameters(filterOptions) where TIncludes : JsonApiIncludes {

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
    ) = RequestParametersWithIncludes(filterOptions, includes)
}
