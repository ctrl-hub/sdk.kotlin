package com.ctrlhub.core.serializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalDateTimeSerializerTest {

    @Serializable
    data class TestData(
        @Serializable(with = LocalDateTimeSerializer::class)
        val dateTime: LocalDateTime
    )

    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
    }

    @Test
    fun `test deserialization of LocalDateTime`() {
        val dateTimeString = "2025-01-15T14:04:34.245708802Z"
        val expectedDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        val jsonString = """
            {
                "dateTime": "$dateTimeString"
            }
        """.trimIndent()

        val result = json.decodeFromString(TestData.serializer(), jsonString)

        assertEquals(expectedDateTime, result.dateTime)
    }

    @Test
    fun `test serialization of LocalDateTime`() {
        val dateTime = LocalDateTime.of(2025, 1, 15, 14, 4, 34, 245708802)
        val expectedJsonString = """
            {
                "dateTime": "2025-01-15T14:04:34.245708802Z"
            }
        """.trimIndent()

        val testData = TestData(dateTime)
        val result = json.encodeToString(TestData.serializer(), testData)

        assertEquals(expectedJsonString, result)
    }
}