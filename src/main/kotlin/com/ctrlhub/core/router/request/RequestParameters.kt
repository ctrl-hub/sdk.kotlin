package com.ctrlhub.core.router.request

data class FilterOption(
    val field: String,
    val value: String
)

/**
 * A class that represents request parameters that can be specified
 * as part of an API request
 */
data class RequestParameters(
    val filterOptions: List<FilterOption> = emptyList(),
    val includes: List<JsonApiIncludes> = emptyList()
) {
    fun toMap(): Map<String, String> {
        val queryParams = mutableMapOf<String, String>()

        if (includes.isNotEmpty()) {
            queryParams.put("include", buildIncludesQueryString(includes))
        }

        filterOptions.forEach { queryParams.put("filter[${it.field}]", it.value) }

        return queryParams
    }

    private fun buildIncludesQueryString(includes: List<JsonApiIncludes>): String {
        if (includes.isEmpty()) {
            return ""
        }

        return includes.joinToString(",") { it.value().lowercase() }
    }
}
