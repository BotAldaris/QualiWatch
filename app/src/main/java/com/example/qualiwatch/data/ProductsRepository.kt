package com.example.qualiwatch.data

import com.example.qualiwatch.data.source.local.OfflineProductDataSource
import com.example.qualiwatch.data.source.network.NetworkProductsRepository
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost

interface ProductsRepository {
    suspend fun getProducts(): List<Product>
    suspend fun postProduct(product: ProductPost)
    suspend fun putProducts(id: String, product: ProductPost)
    suspend fun deleteProduct(product: Product)
    suspend fun syncProduct()
    suspend fun getProductById(id: String): Product
}

class OnOffProductsRepository(
    val offlineProductRepository: OfflineProductDataSource,
    val networkProductsRepository: NetworkProductsRepository,
    val preferencesRepository: UserPreferencesRepository,
) : ProductsRepository {
    override suspend fun getProducts(): List<Product> {
        if (preferencesRepository.getSaveOnline()) {
            return networkProductsRepository.getProducts()
        }
        return offlineProductRepository.getProducts()
    }

    override suspend fun postProduct(product: ProductPost) {
        if (preferencesRepository.getSaveOnline()) {
            networkProductsRepository.postProduct(product)
        } else {
            offlineProductRepository.postProduct(product)
        }
    }

    override suspend fun putProducts(id: String, product: ProductPost) {
        if (preferencesRepository.getSaveOnline()) {
            networkProductsRepository.putProducts(id, product)
        } else {
            offlineProductRepository.putProducts(id, product)
        }
    }

    override suspend fun deleteProduct(product: Product) {
        if (preferencesRepository.getSaveOnline()) {
            networkProductsRepository.deleteProduct(product)
        } else {
            offlineProductRepository.deleteProduct(product)
        }
    }

    override suspend fun syncProduct() {
        val products = offlineProductRepository.getProducts()
        products.map { product ->
            networkProductsRepository.postProduct(
                ProductPost(
                    product.nome,
                    product.lote,
                    product.validade
                )
            )
            offlineProductRepository.deleteProduct(product)
        }
    }

    override suspend fun getProductById(id: String): Product {
        if (preferencesRepository.getSaveOnline()) {
            return networkProductsRepository.getProductById(id)
        }
        return offlineProductRepository.getProductById(id)
    }

}