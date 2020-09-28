package xyz.tetatet.vivaquiz.io.repository.repos.products

sealed class ProductsArgs
open class BaseProductsArgs
class GetProductsArgs() : BaseProductsArgs()

