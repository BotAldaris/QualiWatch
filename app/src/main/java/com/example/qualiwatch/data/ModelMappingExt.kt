package com.example.qualiwatch.data

import com.example.qualiwatch.data.source.local.LocalProduct
import com.example.qualiwatch.data.source.local.NearProduct
import com.example.qualiwatch.model.Product

fun Product.toLocal() = LocalProduct(id, nome, lote, validade)
fun Product.toNear() = NearProduct(id, nome, lote, validade)
fun List<Product>.toLocal() = map(Product::toLocal)
fun List<Product>.toNear() = map(Product::toNear)

fun LocalProduct.toExternal() = Product(id, nome, lote, validade)

@JvmName("localToExternal")
fun List<LocalProduct>.toExternal() = map(LocalProduct::toExternal)

fun NearProduct.toExternal() = Product(id, nome, lote, validade)

@JvmName("nearToExternal")
fun List<NearProduct>.toExternal() = map(NearProduct::toExternal)
