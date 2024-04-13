package com.example.qualiwatch.data

import com.example.qualiwatch.data.source.local.NearExpirationDataSource
import com.example.qualiwatch.data.source.local.NearProduct
import com.example.qualiwatch.data.source.network.NetworkProductsRepository
import com.example.qualiwatch.model.NearProductRequest
import com.example.qualiwatch.model.Product
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface INearExpirationRepository {
    fun getProductsFlow(): Flow<List<Product>>
    suspend fun getProducts(): List<Product>
    suspend fun postProduct(product: NearProduct)
    suspend fun deleteProduct(product: Product)
    suspend fun deleteAll()
    suspend fun syncProduct()
}

class NearExpirationRepository(
    private val nearExpirationRepository: NearExpirationDataSource,
    private val networkProductsRepository: NetworkProductsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : INearExpirationRepository {
    override fun getProductsFlow(): Flow<List<Product>> {
        return nearExpirationRepository.getProductsFlow()
    }

    override suspend fun getProducts(): List<Product> {
        return nearExpirationRepository.getProducts()
    }

    override suspend fun postProduct(product: NearProduct) {
        nearExpirationRepository.postProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        nearExpirationRepository.deleteProduct(product)
    }

    override suspend fun deleteAll() {
        nearExpirationRepository.deleteAll()
    }

    override suspend fun syncProduct() {
        if (!userPreferencesRepository.isExists()) {
            userPreferencesRepository.addInitialUserPreferences()
        }
        val lastUpdate = getProducts()
        val allAlerts =
            networkProductsRepository.getNearProducts(
                NearProductRequest(
                    LocalDateTime.of(
                        2000,
                        1,
                        1,
                        1,
                        1
                    )
                )
            ).toSet()
        val deletedProducts = lastUpdate.subtract(allAlerts)
        for (deletedProduct in deletedProducts) {
            deleteProduct(deletedProduct)
        }
        val body = NearProductRequest(userPreferencesRepository.getLastDate())
        val data = LocalDateTime.now()
        val newProducts = networkProductsRepository.getNearProducts(body)
        for (newProduct in newProducts) {
            postProduct(newProduct.toNear())
        }
        userPreferencesRepository.updateLastUpdate(data)
    }

}