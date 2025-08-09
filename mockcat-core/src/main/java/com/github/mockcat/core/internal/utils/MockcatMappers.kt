package com.github.mockcat.core.internal.utils

import com.github.mockcat.core.internal.data.Mock
import com.github.mockcat.core.internal.data.MockFileEntry
import com.github.mockcat.core.internal.data.MockType
import kotlinx.serialization.json.Json

/**
* This file contains extension functions for mapping between the database entity (`Mock`)
* and the file/network data transfer object (`MockFileEntry`).
* This centralizes the conversion logic into a single source of truth.
*/

// --- Conversion from the File Model TO the Database Model ---
private fun MockFileEntry.toMockEntity(): Mock {
      val determinedType = this.mockType

      return when (determinedType) {
            MockType.STATIC -> Mock(
                id = 0,
                label = this.label,
                url = this.url,
                httpMethod = this.httpMethod,
                isEnabled = this.isEnabled,
                mockType = MockType.STATIC,
                requiredHeaders = this.requiredHeaders,
                responseCode = this.responseCode ?: 200,
                responseBody = this.responseBody?.toString() ?: "",
                delayMs = this.delayMs ?: 200,
                redirectUrl = null,
            )
            MockType.REDIRECT -> Mock(
              id = 0,
              label = this.label,
              url = this.url,
              httpMethod = this.httpMethod,
              isEnabled = this.isEnabled,
              mockType = MockType.REDIRECT,
              requiredHeaders = this.requiredHeaders,
              redirectUrl = this.redirectUrl ?: "",
              responseCode = null,
              responseBody = null,
              delayMs = null,
            )
          }
}

fun List<MockFileEntry>.toMockEntities(): List<Mock> {
      return this.map { it.toMockEntity() }
}


// --- Conversion from the Database Model TO the File Model ---
private fun Mock.toMockFileEntry(): MockFileEntry {
      return MockFileEntry(
        label = this.label,
        url = this.url,
        httpMethod = this.httpMethod,
        isEnabled = this.isEnabled,
        mockType = this.mockType,
        requiredHeaders = this.requiredHeaders,
        responseCode = this.responseCode,
        responseBody = this.responseBody?.let {
              try {
                Json.parseToJsonElement(it)
              } catch (e: Exception) {
                e.printStackTrace()
                null
              }
            },
        delayMs = this.delayMs,
        redirectUrl = this.redirectUrl,
      )
}

fun List<Mock>.toMockFileEntries(): List<MockFileEntry> {
      return this.map { it.toMockFileEntry() }
}
