package com.github.mockcat.core.internal.data

import androidx.room.withTransaction
import com.github.mockcat.core.internal.utils.toMockEntities
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

// Todo: We can abstract this behind interface - MockcatRepository and MockcatRepositoryImpl.
class MockcatRepository (
    private val database: MockcatDatabase,
    private val mockDao: MockDao,
) {
    suspend fun importMocksFromJson(jsonString: String): Int {
        val fileEntries = Json.decodeFromString<MultipleMockEntries>(jsonString).entries
        val mocksToInsert = fileEntries.toMockEntities()
        // When importing from file, simply going for Wipe and Replace
        database.withTransaction {
            mockDao.deleteAll()
            mocksToInsert.forEach { mock ->
                mockDao.insertOrUpdate(mock)
            }
        }

        return mocksToInsert.size
    }

    fun getAllMocks() = mockDao.getAllMocks()

    suspend fun saveMock(mock: Mock) = mockDao.insertOrUpdate(mock)

    suspend fun deleteMock(mock: Mock) = mockDao.delete(mock)

    suspend fun insertOrUpdate(mock: Mock) = mockDao.insertOrUpdate(mock)

    suspend fun deleteAllMocks() = mockDao.deleteAll()

    suspend fun getAllMocksOnce(): List<Mock> = getAllMocks().first()
}
