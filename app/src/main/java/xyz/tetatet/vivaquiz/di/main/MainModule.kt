package xyz.tetatet.vivaquiz.di.main

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.tetatet.vivaquiz.db.DatabaseController
import xyz.tetatet.vivaquiz.controllers.shared.PreferencesRepository
import xyz.tetatet.vivaquiz.di.ViewScope
import xyz.tetatet.vivaquiz.io.repository.FoursquareRepository
import xyz.tetatet.vivaquiz.io.repository.VivaRepository
import xyz.tetatet.vivaquiz.io.repository.repos.foursquare.FoursquareRepo
import xyz.tetatet.vivaquiz.io.repository.repos.products.ProductsRepo
import xyz.tetatet.vivaquiz.ui.main.MainPresenter
import xyz.tetatet.vivaquiz.ui.main.MainPresenterImpl

@Module
class MainModule {

    @ViewScope
    @Provides
    fun provideProductsRepo(vivaRepository: VivaRepository,databaseController: DatabaseController): ProductsRepo =
        ProductsRepo(vivaRepository,databaseController)

    @ViewScope
    @Provides
    fun provideFoursquareRepo(foursquareRepository: FoursquareRepository): FoursquareRepo =
        FoursquareRepo(foursquareRepository)

    @ViewScope
    @Provides
    fun provideMainPresenter(context: Context,
                             productsRepo: ProductsRepo,
                             foursquareRepo: FoursquareRepo,
                             preferencesRepository: PreferencesRepository,
                             databaseController: DatabaseController): MainPresenter =
        MainPresenterImpl(context,productsRepo,foursquareRepo,preferencesRepository,databaseController)
}