package com.ctrlhub.core.router.request

internal fun rqlValue(v: Any?): String = when (v) {
    null -> "null"
    is Boolean, is Number -> v.toString()
    else -> v.toString()
}

sealed class Comparison(
    private val op: String,
    private val field: String,
    private val value: Any?
) : FilterOption {

    override fun format(): String =
        "$op($field,${rqlValue(value)})"
}

class Eq(field: String, value: Any?) : Comparison("eq", field, value)
class Ne(field: String, value: Any?) : Comparison("ne", field, value)
class Gt(field: String, value: Any?) : Comparison("gt", field, value)
class Gte(field: String, value: Any?) : Comparison("gte", field, value)
class Lt(field: String, value: Any?) : Comparison("lt", field, value)
class Lte(field: String, value: Any?) : Comparison("lte", field, value)

class In(
    private val field: String,
    private val values: Collection<Any?>
) : FilterOption {

    override fun format(): String =
        "in($field,[${values.joinToString(",") { rqlValue(it) }}])"
}

class Between(
    private val field: String,
    private val from: Any?,
    private val to: Any?
) : FilterOption {

    override fun format(): String =
        "between($field,${rqlValue(from)},${rqlValue(to)})"
}

class And(private val parts: List<FilterOption>) : FilterOption {
    override fun format(): String =
        "and(${parts.joinToString(",") { it.format() }})"
}

class Or(private val parts: List<FilterOption>) : FilterOption {
    override fun format(): String =
        "or(${parts.joinToString(",") { it.format() }})"
}

class RawFilter(private val value: String) : FilterOption {
    override fun format(): String = value
}