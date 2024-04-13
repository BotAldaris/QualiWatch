package com.example.qualiwatch.fake

import com.example.qualiwatch.data.ProductsRepository
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost

class FakeOnOffProductRepository : ProductsRepository {
    override suspend fun getProducts(): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun postProduct(product: ProductPost) {
        TODO("Not yet implemented")
    }

    override suspend fun putProducts(id: String, product: ProductPost) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun syncProduct() {
        TODO("Not yet implemented")
    }
}