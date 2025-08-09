package com.github.mockcat.core.internal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Mock::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(MockTypeConverters::class)
abstract class MockcatDatabase : RoomDatabase() {

    abstract fun mockDao(): MockDao

    companion object {
        const val DATABASE_NAME = "mockcat_db"
    }
}
