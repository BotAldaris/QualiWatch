package com.example.qualiwatch.data

import com.example.qualiwatch.data.source.network.NetworkProductsRepository
import com.example.qualiwatch.model.NearProductRequest
import com.example.qualiwatch.model.Product
import java.time.LocalDateTime

interface INearExpirationRepository {
    suspend fun getProducts(): List<Product>
}

class NearExpirationRepository(
    private val networkProductsRepository: NetworkProductsRepository,
) : INearExpirationRepository {
    override suspend fun getProducts(): List<Product> {
        return networkProductsRepository.getNearProducts(
            NearProductRequest(
                LocalDateTime.of(
                    2000,
                    1,
                    1,
                    1,
                    1,
                )
            )
        )
    }
}