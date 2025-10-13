package com.ctrlhub.core.router.request

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private enum class TestIncludes(private val v: String) : JsonApiIncludes {
    AUTHOR("author"), FORM("form");
    override fun value() = v
}

class RequestParametersTest {

    @Test
    fun `toMap includes offset and limit`() {
        val request = RequestParameters(offset = 5, limit = 25)
        val map = request.toMap()

        assertEquals("5", map["offset"])
        assertEquals("25", map["limit"])
    }

    @Test
    fun `single filter option produces filter key`() {
        val request = RequestParameters(
            filterOptions = listOf(FilterOption("status", "open"))
        )

        val map = request.toMap()
        assertEquals("open", map["filter[status]"])
    }

    @Test
    fun `multiple filter options for same field are appended with comma`() {
        val request = RequestParameters(
            filterOptions = listOf(
                FilterOption("status", "open"),
                FilterOption("status", "closed"),
            )
        )

        val map = request.toMap()
        assertEquals("open,closed", map["filter[status]"])
    }

    @Test
    fun `anonymous filter options append to filter key`() {
        val request = RequestParameters(
            filterOptions = listOf(
                FilterOption(null, "a"),
                FilterOption(null, "b")
            )
        )

        val map = request.toMap()
        assertEquals("a,b", map["filter"])
    }

    @Test
    fun `includes are serialized when using RequestParametersWithIncludes`() {
        val request = RequestParametersWithIncludes<TestIncludes>(
            includes = listOf(TestIncludes.AUTHOR, TestIncludes.FORM)
        )

        val map = request.toMap()
        assertNotNull(map["include"]) // something present
        assertEquals("author,form", map["include"])
    }
}
