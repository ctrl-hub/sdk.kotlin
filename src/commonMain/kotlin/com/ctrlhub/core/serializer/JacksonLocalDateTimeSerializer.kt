package com.ctrlhub.core.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

class JacksonLocalDateTimeSerializer @JvmOverloads constructor(t: Class<LocalDateTime>? = null) : StdSerializer<LocalDateTime>(t) {
    override fun serialize(
        value: LocalDateTime?,
        gen: JsonGenerator?,
        provider: SerializerProvider?
    ) {
        value?.let { gen?.writeString(value.atOffset(ZoneOffset.UTC).format(formatter)) }
    }
}

class JacksonLocalDateTimeDeserializer @JvmOverloads constructor(t: Class<LocalDateTime>? = null) : StdDeserializer<LocalDateTime>(t) {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext?
    ): LocalDateTime? {
        val node: JsonNode = p.codec.readTree(p)
        val dateString = node.asText()

        return LocalDateTime.parse(dateString, formatter)
    }
}