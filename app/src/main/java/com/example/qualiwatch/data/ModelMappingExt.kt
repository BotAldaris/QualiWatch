package com.example.qualiwatch.data

import com.example.qualiwatch.data.source.local.LocalProduct
import com.example.qualiwatch.model.Product

fun Product.toLocal() = LocalProduct(id, nome, lote, validade)
fun List<Product>.toLocal() = map(Product::toLocal)

fun LocalProduct.toExternal() = Product(id, nome, lote, validade)

@JvmName("localToExternal")
fun List<LocalProduct>.toExternal() = map(LocalProduct::toExternal)

