package com.example.qualiwatch.data

import com.example.qualiwatch.data.source.local.UserPreferences
import com.example.qualiwatch.data.source.local.UserPreferencesDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class UserPreferencesRepository(private val userPreferencesDao: UserPreferencesDao) {
    suspend fun isExists(): Boolean = userPreferencesDao.isExists()
    suspend fun getUserPreferences(): UserPreferences = userPreferencesDao.getUserPreferences()
    suspend fun getLastDate(): LocalDateTime = userPreferencesDao.getLastDate()
    suspend fun getSaveOnline(): Boolean = userPreferencesDao.getSaveOnline()

    fun getDarkFlow(): Flow<Boolean> = userPreferencesDao.getDarkFlow()
    fun getUserPreferencesFlow(): Flow<UserPreferences> =
        userPreferencesDao.getUserPreferencesFlow()

    suspend fun updateLastUpdate(lastUpdate: LocalDateTime) =
        userPreferencesDao.updateLastUpdate(lastUpdate)

    suspend fun updateDark(dark: Boolean) =
        userPreferencesDao.updateDark(dark)

    suspend fun updateSaveOnline(online: Boolean) =
        userPreferencesDao.updateSaveOnline(online)

    suspend fun addInitialUserPreferences() = userPreferencesDao.upsert(
        UserPreferences(
            1, true,
            LocalDateTime.of(2000, 1, 1, 0, 0), true,
        )
    )

    suspend fun upsert(userPreferences: UserPreferences) =
        userPreferencesDao.upsert(userPreferences)
}