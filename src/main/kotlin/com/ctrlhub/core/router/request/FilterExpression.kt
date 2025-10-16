package com.ctrlhub.core.router.request

sealed interface FilterOption {
    fun format(): String
}

class FieldFilterExpression<T>(val field: String, val value: T) : FilterOption {
    private fun quoteIfNeeded(v: Any?): String = when (v) {
        is String -> "'${v.replace("'", "\\'")}'"
        null -> "''"
        else -> v.toString()
    }

    override fun format(): String {
        return when (value) {
            is List<*> -> {
                val inner = value.joinToString(",") { quoteIfNeeded(it) }
                "$field($inner)"
            }
            else -> "$field(${quoteIfNeeded(value)})"
        }
    }
}

class ValueFilterExpression(val value: String) : FilterOption {
    override fun format(): String = value
}

class AndExpression(val parts: List<FilterOption>) : FilterOption {
    override fun format(): String = "and(${parts.joinToString(",") { it.format() }})"
}

@Suppress("unused")
class OrExpression(val parts: List<FilterOption>) : FilterOption {
    override fun format(): String = "or(${parts.joinToString(",") { it.format() }})"
}