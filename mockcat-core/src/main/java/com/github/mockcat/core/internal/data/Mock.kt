package com.github.mockcat.core.internal.data

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
@Entity(tableName = "mocks")
@Stable
data class Mock(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val label: String = "",
    val httpMethod: String,
    val isEnabled: Boolean = true,
    val mockType: MockType = MockType.STATIC,
    val responseCode: Int? = null,
    val responseBody: String? = null,
    val delayMs: Long? = null,
    val redirectUrl: String? = null,
    val requiredHeaders: Map<String, String>? = null,
)

@Serializable
data class MockFileEntry(
    val url: String,
    val label: String = "",
    val httpMethod: String,
    val isEnabled: Boolean = true,
    val mockType: MockType = MockType.STATIC,
    val responseCode: Int? = null,
    val responseBody: JsonElement? = null,
    val delayMs: Long? = null,
    val redirectUrl: String? = null,
    val requiredHeaders: Map<String, String>? = null,
)

@Serializable
data class MultipleMockEntries(
    val entries: List<MockFileEntry>,
)


enum class MockType {
    STATIC,
    REDIRECT,
}
