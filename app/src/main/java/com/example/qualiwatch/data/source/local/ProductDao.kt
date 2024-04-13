package com.example.qualiwatch.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAll(): List<LocalProduct>

    @Query("Select * FROM products WHERE id = :productId")
    suspend fun getById(productId: String): LocalProduct

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: LocalProduct)

    @Update
    suspend fun update(product: LocalProduct)

    @Delete
    suspend fun delete(product: LocalProduct)
}