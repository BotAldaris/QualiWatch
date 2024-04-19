package com.example.qualiwatch.data.source.network

import com.example.qualiwatch.model.NearProductRequest
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost
import com.example.qualiwatch.network.ProductsApiService

class NetworkProductsRepository(private val productsApiService: ProductsApiService) {
    suspend fun getProducts(): List<Product> = productsApiService.getProducts()
    suspend fun postProduct(product: ProductPost) =
        productsApiService.postProducts(product)

    suspend fun putProducts(id: String, product: ProductPost) {
        productsApiService.putProducts(id, product)
    }

    suspend fun getNearProducts(nearProductRequest: NearProductRequest) =
        productsApiService.getNearProducts(nearProductRequest)

    suspend fun deleteProduct(product: Product) =
        productsApiService.deleteProducts(product.id)

    suspend fun getProductById(id: String): Product = productsApiService.getProductById(id)
}

