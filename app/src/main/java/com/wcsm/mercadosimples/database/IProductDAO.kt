package com.wcsm.mercadosimples.database

import com.wcsm.mercadosimples.model.Product

interface IProductDAO {
    fun save(product: Product): Boolean
    fun delete(productId: String): Boolean
    fun deleteALlProducts(): Boolean
    fun listProducts(): MutableList<Product>

}