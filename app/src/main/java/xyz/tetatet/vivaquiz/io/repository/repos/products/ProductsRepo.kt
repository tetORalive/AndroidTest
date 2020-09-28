package xyz.tetatet.vivaquiz.io.repository.repos.products

import android.content.Context
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import xyz.tetatet.vivaquiz.db.DatabaseController
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.io.model.viva.ProductsResponse
import xyz.tetatet.vivaquiz.io.repository.VivaRepository
import xyz.tetatet.vivaquiz.io.repository.base.repolivedata.CoroutineRepo


class ProductsRepo(
    private val vivaRepository: VivaRepository,
    private val databaseController: DatabaseController
) : CoroutineRepo<MutableList<Product?>, BaseProductsArgs>() {

    override suspend fun api(context: Context, args: BaseProductsArgs): MutableList<Product?>? = coroutineScope {
        when (args) {
            is GetProductsArgs -> {
                vivaRepository.getProductsAsync().await()
                    .also {
                        it?.let { products ->
                            databaseController.removeAllProducts()
                            databaseController.insertProducts(products)
                        }
                    }
                    .let { response -> response }
            }
            else -> throw IllegalArgumentException("Illegal argument for GET Products Request")
        }
    }
}
