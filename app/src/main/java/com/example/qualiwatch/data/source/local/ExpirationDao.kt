package com.example.qualiwatch.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpirationDao {
    @Query("SELECT * FROM expiration")
    fun getAllFlow(): Flow<List<NearProduct>>

    @Query("SELECT * FROM expiration")
    suspend fun getAll(): List<NearProduct>

    @Query("Select * FROM expiration WHERE id = :productId")
    fun getById(productId: String): Flow<NearProduct>

    @Upsert
    suspend fun insert(product: NearProduct)

    @Delete
    suspend fun delete(product: NearProduct)

    @Query("DELETE FROM expiration")
    suspend fun deleteAll()
}
