package com.ctrlhub.core.router.request

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RequestParametersFiltersTest {

    @Test
    fun `and of two field expressions formats correctly`() {
        val expr = AndExpression(
            listOf(
                FieldFilterExpression("status", listOf("open")),
                FieldFilterExpression("category", listOf("news"))
            )
        )

        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals("and(status('open'),category('news'))", map["filter"])
    }

    @Test
    fun `and of functions formats correctly`() {
        val expr = AndExpression(
            listOf(
                ValueFilterExpression("is_latest()"),
                ValueFilterExpression("no_start()"),
            )
        )

        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals("and(is_latest(),no_start())", map["filter"])
    }

    @Test
    fun `mixed and expression with field and function`() {
        val expr = AndExpression(
            listOf(
                FieldFilterExpression("status", listOf("active")),
                ValueFilterExpression("is_latest()")
            )
        )

        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals("and(status('active'),is_latest())", map["filter"])
    }
}

