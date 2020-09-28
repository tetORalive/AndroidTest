package xyz.tetatet.vivaquiz.ui.main

import androidx.lifecycle.Observer
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.io.model.viva.ProductsResponse
import xyz.tetatet.vivaquiz.io.repository.base.NetworkState
import xyz.tetatet.vivaquiz.io.repository.base.repolivedata.RepoResponse
import xyz.tetatet.vivaquiz.io.repository.repos.foursquare.FoursquarePlacesArgs
import xyz.tetatet.vivaquiz.io.repository.repos.products.BaseProductsArgs
import xyz.tetatet.vivaquiz.io.repository.repos.products.ProductsArgs
import xyz.tetatet.vivaquiz.ui.base.BasePresenter

interface MainPresenter : BasePresenter<MainView> {

    val isDarkThemeObserver : Observer<Boolean>
    val categoryNumberObserver : Observer<Int>

    val productsResponse: RepoResponse<MutableList<Product?>, BaseProductsArgs>
    val mProductsObserver: Observer<MutableList<Product?>>
    val mProductsNetworkStateObserver: Observer<NetworkState<MutableList<Product?>, BaseProductsArgs>>
    fun getProducts()

    val foursquareResponse: RepoResponse<FoursquareResponse, FoursquarePlacesArgs>
    val mFoursquareObserver: Observer<FoursquareResponse>
    val mFoursquareNetworkStateObserver: Observer<NetworkState<FoursquareResponse, FoursquarePlacesArgs>>
    fun getFoursquarePlaces(latlng:String?, foursquareApiId:String?, foursquareApiSecret:String?, category: Int?)
}