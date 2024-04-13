package com.example.qualiwatch.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface UserPreferencesDao {
    @Query("SELECT EXISTS(SELECT * FROM userpreferences WHERE id = 1)")
    suspend fun isExists(): Boolean

    @Query("SELECT * FROM userpreferences WHERE id = 1")
    suspend fun getUserPreferences(): UserPreferences

    @Query("SELECT lastUpdate FROM userpreferences WHERE id = 1")
    suspend fun getLastDate(): LocalDateTime

    @Query("SELECT online FROM userpreferences WHERE id = 1")
    suspend fun getSaveOnline(): Boolean

    @Query("SELECT dark FROM userpreferences WHERE id = 1")

    fun getDarkFlow(): Flow<Boolean>

    @Query("SELECT * FROM userpreferences WHERE id = 1")
    fun getUserPreferencesFlow(): Flow<UserPreferences>

    @Query("UPDATE userpreferences SET lastUpdate = :lastUpdate WHERE id = 1")
    suspend fun updateLastUpdate(lastUpdate: LocalDateTime)

    @Query("UPDATE userpreferences SET dark = :dark WHERE id = 1")
    suspend fun updateDark(dark: Boolean)

    @Query("UPDATE userpreferences SET online = :online WHERE id = 1")
    suspend fun updateSaveOnline(online: Boolean)

    @Upsert
    suspend fun upsert(userPreferences: UserPreferences)

}