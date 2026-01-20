package com.ctrlhub.core.router.request

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RequestParametersFiltersTest {

    @Test
    fun `and of two field expressions formats correctly`() {
        val expr = And(
            listOf(
                Eq("status", "open"),
                Eq("category", "news")
            )
        )

        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals("and(eq(status,'open'),eq(category,'news'))", map["filter"])
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
                FieldFilterExpression("status", "active"),
                ValueFilterExpression("is_latest()")
            )
        )

        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals("and(status('active'),is_latest())", map["filter"])
    }

    @Test
    fun `field list formats correctly`() {
        val ids = listOf(
            "06e55d1c-f816-48b2-b11b-debdb3115b2f",
            "5f43b904-8d5b-4b81-9258-b14aafe858d4",
            "9b31bdd1-2785-4419-b1b3-36ccd35d22c6"
        )

        val expr = FieldFilterExpression("payload_operations", ids)
        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals(
            "payload_operations('06e55d1c-f816-48b2-b11b-debdb3115b2f','5f43b904-8d5b-4b81-9258-b14aafe858d4','9b31bdd1-2785-4419-b1b3-36ccd35d22c6')",
            map["filter"]
        )
    }

    @Test
    fun `or of field list and single field formats correctly`() {
        val ids = listOf(
            "06e55d1c-f816-48b2-b11b-debdb3115b2f",
            "5f43b904-8d5b-4b81-9258-b14aafe858d4"
        )

        val expr = OrExpression(
            listOf(
                FieldFilterExpression("payload_operations", ids),
                FieldFilterExpression("category", "updates")
            )
        )

        val params = RequestParameters(filters = listOf(expr))
        val map = params.toMap()

        assertEquals(
            "or(payload_operations('06e55d1c-f816-48b2-b11b-debdb3115b2f','5f43b904-8d5b-4b81-9258-b14aafe858d4'),category('updates'))",
            map["filter"]
        )
    }
}
