package com.example.qualiwatch.model

import com.example.qualiwatch.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NearProductRequest(
    @Serializable(with = DateSerializer::class)
    val UltimaAtulizacao: LocalDateTime
)
