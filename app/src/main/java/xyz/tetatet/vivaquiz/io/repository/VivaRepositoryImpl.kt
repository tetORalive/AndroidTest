package xyz.tetatet.vivaquiz.io.repository

import kotlinx.coroutines.Deferred
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.io.model.viva.ProductsResponse
import xyz.tetatet.vivaquiz.io.network.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VivaRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) : VivaRepository {

    override fun getProductsAsync(): Deferred<MutableList<Product?>?> =
        apiInterface.getProductsAsync()
}

