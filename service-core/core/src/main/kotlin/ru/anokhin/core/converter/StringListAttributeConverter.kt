package ru.anokhin.core.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Converter
class StringListAttributeConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(attribute: List<String>?): String? =
        attribute?.let(Json::encodeToString)

    override fun convertToEntityAttribute(dbData: String?): List<String> =
        dbData?.let(Json::decodeFromString) ?: emptyList()
}
