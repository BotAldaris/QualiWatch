package com.example.qualiwatch.model

import com.example.qualiwatch.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class Product(
    val id: String,
    val nome: String,
    val lote: String,
    @Serializable(with = DateSerializer::class)
    val validade: LocalDateTime
)

@Serializable
data class ProductPost(
    val nome: String,
    val lote: String,
    @Serializable(with = DateSerializer::class)
    val validade: LocalDateTime
)
