package com.example.qualiwatch.data.source.network

import com.example.qualiwatch.model.NearProductRequest
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NetworkProductsRepository(private val client: HttpClient,private val urlbase: String) {
    suspend fun getProducts(): List<Product> = client.get("$urlbase/produtos").body()
    suspend fun postProduct(product: ProductPost) =
        client.post("$urlbase/produtos"){
            contentType(ContentType.Application.Json)
            setBody(product)}

    suspend fun putProducts(id: String, product: ProductPost) {
        client.put("$urlbase/produtos/$id") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }
    }
    suspend fun getNearProducts(nearProductRequest: NearProductRequest) :List<Product> =
        client.post("$urlbase/produtos/validade"){contentType(ContentType.Application.Json)
            setBody(nearProductRequest)}.body()

    suspend fun deleteProduct(product: Product) =
        client.delete("$urlbase/produtos/${product.id}")

    suspend fun getProductById(id: String): Product = client.get("$urlbase/produtos/$id").body()
}

