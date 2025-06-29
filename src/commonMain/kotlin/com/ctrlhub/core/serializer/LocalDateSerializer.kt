package com.ctrlhub.core.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class LocalDateSerializer : KSerializer<LocalDate?> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: LocalDate?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeString(value.format(formatter))
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate? {
        val stringValue = decoder.decodeString()
        return if (stringValue.isEmpty()) {
            null
        } else {
            LocalDate.parse(stringValue, formatter)
        }
    }
}