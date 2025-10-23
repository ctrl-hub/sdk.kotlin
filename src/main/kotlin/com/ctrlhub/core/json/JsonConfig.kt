package com.ctrlhub.core.json

import com.ctrlhub.core.serializer.JacksonLocalDateTimeDeserializer
import com.ctrlhub.core.serializer.JacksonLocalDateTimeSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import java.time.LocalDateTime

object JsonConfig {
    fun getMapper(): ObjectMapper {
        val module = SimpleModule().apply {
            addSerializer(LocalDateTime::class.java, JacksonLocalDateTimeSerializer())
            addDeserializer(LocalDateTime::class.java, JacksonLocalDateTimeDeserializer())
        }

        return ObjectMapper().apply {
            registerModule(JavaTimeModule())
            registerModule(module)
            registerModule(kotlinModule())
        }
    }
}