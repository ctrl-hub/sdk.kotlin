package com.ctrlhub.core.serializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalDateSerializerTest {
    @Serializable
    data class TestData(
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate?
    )

    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun `test deserialization of valid LocalDate`() {
        val jsonString = """
            {
                "date": "2025-01-15"
            }
        """.trimIndent()

        val result = json.decodeFromString(TestData.serializer(), jsonString)
        val expectedDate = LocalDate.of(2025, 1, 15)

        assertEquals(expectedDate, result.date)
    }

    @Test
    fun `test deserialization of null LocalDate`() {
        val jsonString = """
            {
                "date": null
            }
        """.trimIndent()

        val result = json.decodeFromString(TestData.serializer(), jsonString)

        assertEquals(null, result.date)
    }

    @Test
    fun `test serialization of valid LocalDate`() {
        val testData = TestData(LocalDate.of(2025, 1, 15))
        val expectedJsonString = """
            {
                "date": "2025-01-15"
            }
        """.trimIndent()

        val result = json.encodeToString(TestData.serializer(), testData)

        assertEquals(expectedJsonString, result)
    }

    @Test
    fun `test serialization of null LocalDate`() {
        val testData = TestData(null)
        val expectedJsonString = """
            {
                "date": null
            }
        """.trimIndent()

        val result = json.encodeToString(TestData.serializer(), testData)

        assertEquals(expectedJsonString, result)
    }
}