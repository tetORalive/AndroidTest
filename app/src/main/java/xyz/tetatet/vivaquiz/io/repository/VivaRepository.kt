package xyz.tetatet.vivaquiz.io.repository

import kotlinx.coroutines.Deferred
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.io.model.viva.ProductsResponse

interface VivaRepository {

    fun getProductsAsync(): Deferred<MutableList<Product?>?>

}