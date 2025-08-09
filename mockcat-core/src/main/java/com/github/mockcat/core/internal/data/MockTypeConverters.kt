package com.github.mockcat.core.internal.data

import androidx.room.TypeConverter
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class MockTypeConverters {
    @TypeConverter
    fun fromMockType(value: MockType): String = value.name

    @TypeConverter
    fun toMockType(value: String): MockType = MockType.valueOf(value)

    @OptIn(InternalSerializationApi::class)
    @TypeConverter
    fun fromStringMap(headers: Map<String, String>?): String? {
        if (headers == null) {
            return null
        }
        return Json.encodeToString(
            serializer = MapSerializer(
                keySerializer = String.serializer(),
                valueSerializer = String.serializer(),
            ),
            value = headers,
        )
    }

    @TypeConverter
    fun toStringMap(jsonString: String?): Map<String, String>? {
        if (jsonString == null) {
            return null
        }
        return Json.decodeFromString(jsonString)
    }
}
