package com.example.qualiwatch.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "userpreferences")
class UserPreferences(
    @PrimaryKey val id: Int,
    val dark: Boolean,
    val lastUpdate: LocalDateTime,
    val online: Boolean,
)
