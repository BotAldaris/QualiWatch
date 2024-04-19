package com.example.qualiwatch.data.source.local

import com.example.qualiwatch.data.toExternal
import com.example.qualiwatch.data.toLocal
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost
import java.util.UUID

class OfflineProductDataSource(
    private val productDao: ProductDao,
) {
    suspend fun getProducts(): List<Product> = productDao.getAll().toExternal()
    suspend fun postProduct(product: ProductPost) {
        val productId = UUID.randomUUID().toString()
        val localProduct = LocalProduct(productId, product.nome, product.lote, product.validade)
        productDao.insert(localProduct)
    }

    suspend fun putProducts(id: String, product: ProductPost) {
        productDao.update(LocalProduct(id, product.nome, product.lote, product.validade))
    }

    suspend fun deleteProduct(product: Product) = productDao.delete(product.toLocal())
    suspend fun getProductById(id: String): Product = productDao.getById(id).toExternal()
}