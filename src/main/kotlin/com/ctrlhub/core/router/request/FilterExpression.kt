package com.ctrlhub.core.router.request

sealed interface FilterOption {
    fun format(): String
}

class FieldFilterExpression(val field: String, val value: String) : FilterOption {
    override fun format(): String = "${field}('$value')"
}

class ValueFilterExpression(val value: String) : FilterOption {
    override fun format(): String = value
}

class AndExpression(val parts: List<FilterOption>) : FilterOption {
    override fun format(): String = "and(${parts.joinToString(",") { it.format() }})"
}

class OrExpression(val parts: List<FilterOption>) : FilterOption {
    override fun format(): String = "or(${parts.joinToString(",") { it.format() }})"
}