package com.github.mockcat.core.internal.utils

import android.content.Context
import android.net.Uri


class MockcatFileHandler(
    private val context: Context,
) {

    /**
     * Reads the text content from a given URI and returns it inside a Result.
     */
    fun readTextFromUri(uri: Uri): Result<String> {
        return runCatching {
            context.contentResolver.openInputStream(uri)?.use {
                it.bufferedReader().readText()
            } ?: throw Exception("ContentResolver returned a null InputStream.")
        }
    }

    /**
     * Writes a given string of content to a destination URI.
     */
    fun writeTextToUri(uri: Uri, content: String): Result<Unit> {
        return runCatching {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(content.toByteArray(Charsets.UTF_8))
            } ?: throw Exception("Failed to open output stream.")
        }
    }
}
