package xyz.tetatet.vivaquiz.db

import xyz.tetatet.vivaquiz.io.model.viva.Product
import javax.inject.Singleton

@Singleton
class DatabaseController(private val productsDatabase: ProductsDatabase) {

    /*Title --------------------------- Products -------------------------------------------*/

    fun insertProducts(products: MutableList<Product?>) =
        productsDatabase.daoProducts().insertMultipleListRecord(products)

    fun getProducts(): MutableList<Product?> =
        productsDatabase.daoProducts().fetchAllData()

    fun removeSingleProduct(product: Product) =
        productsDatabase.daoProducts().deleteRecord(product)

    fun removeAllProducts() =
        productsDatabase.daoProducts().deleteAllRecords()

}
