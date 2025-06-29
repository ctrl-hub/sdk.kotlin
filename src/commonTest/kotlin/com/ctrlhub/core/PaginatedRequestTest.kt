package com.ctrlhub.core

import com.ctrlhub.core.router.request.RequestParameters
import kotlin.test.Test
import kotlin.test.assertEquals

class PaginatedRequestTest {
    @Test
    fun `can set limit correctly in request`() {
        val request = RequestParameters(
            limit = 10
        )

        val queryParams = request.toMap()
        assertEquals("10", queryParams["limit"])
    }

    @Test
    fun `can set offset correctly in request`() {
        val request = RequestParameters(
            offset = 10
        )

        val queryParams = request.toMap()
        assertEquals("10", queryParams["offset"])
    }
}