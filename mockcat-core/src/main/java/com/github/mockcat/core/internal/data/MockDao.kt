package com.github.mockcat.core.internal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(mock: Mock)

    @Delete
    suspend fun delete(mock: Mock)

    @Query("DELETE FROM mocks")
    suspend fun deleteAll()

    @Query("SELECT * FROM mocks ORDER BY url ASC")
    fun getAllMocks(): Flow<List<Mock>>

    @Query("SELECT * FROM mocks WHERE isEnabled = 1 AND url = :url AND httpMethod = :method")
    suspend fun findMatchingMockCandidates(url: String, method: String): List<Mock>
}
