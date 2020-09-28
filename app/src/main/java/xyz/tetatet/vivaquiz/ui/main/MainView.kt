package xyz.tetatet.vivaquiz.ui.main

import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.ui.base.BaseView

interface MainView : BaseView {

    fun applyTheme(isDarkTheme: Boolean)
    fun applyCategory(category: Int)

    fun productsSuccess(products:MutableList<Product?>)
    fun foursquareSuccess(foursquare: FoursquareResponse)
    fun showProgressLoading()
    fun dismissProgressLoading()

}