package xyz.tetatet.vivaquiz.ui.main

import android.content.Context
import androidx.lifecycle.Observer
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.controllers.shared.PreferencesRepository
import xyz.tetatet.vivaquiz.db.DatabaseController
import xyz.tetatet.vivaquiz.io.model.fourquare.Category
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.io.repository.base.NetworkState
import xyz.tetatet.vivaquiz.io.repository.base.NetworkStatus
import xyz.tetatet.vivaquiz.io.repository.base.repolivedata.RepoResponse
import xyz.tetatet.vivaquiz.io.repository.repos.foursquare.FoursquarePlacesArgs
import xyz.tetatet.vivaquiz.io.repository.repos.foursquare.FoursquarePlacesFromLatLngArgs
import xyz.tetatet.vivaquiz.io.repository.repos.foursquare.FoursquareRepo
import xyz.tetatet.vivaquiz.io.repository.repos.products.BaseProductsArgs
import xyz.tetatet.vivaquiz.io.repository.repos.products.GetProductsArgs
import xyz.tetatet.vivaquiz.io.repository.repos.products.ProductsRepo
import xyz.tetatet.vivaquiz.ui.base.BasePresenterImpl
import javax.inject.Inject

class MainPresenterImpl @Inject constructor(
    private val context: Context,
    private val productsRepo: ProductsRepo,
    private val foursquareRepo: FoursquareRepo,
    private val preferencesRepository: PreferencesRepository,
    private val databaseController: DatabaseController
) : BasePresenterImpl<MainView>(), MainPresenter {

    override val isDarkThemeObserver = Observer<Boolean> {
        getView()?.applyTheme(it)
    }
    override val categoryNumberObserver = Observer<Int> {
        getView()?.applyCategory(it)
    }

    override val productsResponse: RepoResponse<MutableList<Product?>, BaseProductsArgs> =
        productsRepo.mResponse

    override val mProductsObserver: Observer<MutableList<Product?>> = Observer {
        when (it) {
            is MutableList<Product?> -> {
                getView()?.productsSuccess(it)
            }
        }
    }

    override val mProductsNetworkStateObserver =
        Observer<NetworkState<MutableList<Product?>, BaseProductsArgs>> {
            when (it?.status) {
                NetworkStatus.LOADING -> {
                    getView()?.showProgressLoading()
                }
                NetworkStatus.FAILED -> {
                    getView()?.dismissProgressLoading()
                    if (it.requestArgs is GetProductsArgs) it.throwable?.run {
                        getView()?.handleExceptionRes(
                            this,
                            Pair(R.string.errors_retry, { getProducts() }))
                    }
                }
                else -> {
                    getView()?.dismissProgressLoading()
                }
            }
        }

    override fun getProducts() {
        productsRepo.request(context, GetProductsArgs())
    }

    override val foursquareResponse: RepoResponse<FoursquareResponse, FoursquarePlacesArgs> =
        foursquareRepo.mResponse

    override val mFoursquareObserver: Observer<FoursquareResponse> = Observer {
        when (it) {
            is FoursquareResponse -> {
                getView()?.foursquareSuccess(it)
            }
        }
    }

    override val mFoursquareNetworkStateObserver =
        Observer<NetworkState<FoursquareResponse, FoursquarePlacesArgs>> {
            when (it?.status) {
                NetworkStatus.LOADING -> {
                    getView()?.showLoading()
                }
                NetworkStatus.FAILED -> {
                    getView()?.dismissLoading()
                    if (it.requestArgs is FoursquarePlacesFromLatLngArgs) it.throwable?.run {
                        getView()?.handleExceptionRes(
                            this,
                            Pair(R.string.errors_retry, {
                                getFoursquarePlaces(it.requestArgs.latlng, it.requestArgs.foursquareApiId, it.requestArgs.foursquareApiSecret,it?.requestArgs.category)
                            })
                        )
                    }
                }
                else -> { getView()?.dismissLoading() }
            }
        }

    override fun getFoursquarePlaces(
        latlng: String?,
        foursquareApiId: String?,
        foursquareApiSecret: String?, category: Int?) =
        foursquareRepo.request(
            context,
            FoursquarePlacesFromLatLngArgs(latlng, foursquareApiId, foursquareApiSecret,category)
        )

    override fun onCleared() {
        productsResponse.viewModelCleared()
        foursquareResponse.viewModelCleared()
        super.onCleared()
    }
}

