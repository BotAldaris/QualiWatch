package com.example.qualiwatch.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "expiration")
data class NearProduct(
    @PrimaryKey val id: String,
    val nome: String,
    val lote: String,
    val validade: LocalDateTime
)
