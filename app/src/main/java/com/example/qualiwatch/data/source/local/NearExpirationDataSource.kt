package com.example.qualiwatch.data.source.local

import com.example.qualiwatch.data.toExternal
import com.example.qualiwatch.data.toNear
import com.example.qualiwatch.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NearExpirationDataSource(private val expirationDao: ExpirationDao) {
    fun getProductsFlow(): Flow<List<Product>> = expirationDao.getAllFlow().map { it.toExternal() }
    suspend fun getProducts(): List<Product> = expirationDao.getAll().map { it.toExternal() }
    suspend fun postProduct(product: NearProduct) {
        expirationDao.insert(product)
    }

    suspend fun deleteProduct(product: Product) = expirationDao.delete(product.toNear())
    suspend fun deleteAll() = expirationDao.deleteAll()
}