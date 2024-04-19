package com.example.qualiwatch.network

import com.example.qualiwatch.model.NearProductRequest
import com.example.qualiwatch.model.Product
import com.example.qualiwatch.model.ProductPost
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductsApiService {
    @GET("produtos")
    suspend fun getProducts(): List<Product>

    @POST("produtos")
    suspend fun postProducts(@Body product: ProductPost)

    @POST("produtos/validade")
    suspend fun getNearProducts(@Body nearProductRequest: NearProductRequest): List<Product>

    @PUT("produtos/{id}")
    suspend fun putProducts(@Path("id") id: String, @Body product: ProductPost)

    @DELETE("produtos/{id}")
    suspend fun deleteProducts(@Path("id") id: String)

    @GET("produtos/{id}")
    suspend fun getProductById(@Path("id") id: String): Product
}