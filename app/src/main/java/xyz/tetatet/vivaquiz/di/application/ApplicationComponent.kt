package xyz.tetatet.vivaquiz.di.application

import android.content.Context
import dagger.Component
import xyz.tetatet.vivaquiz.db.DatabaseController
import xyz.tetatet.vivaquiz.db.ProductsDatabase
import xyz.tetatet.vivaquiz.io.network.ApiInterface
import xyz.tetatet.vivaquiz.io.repository.FoursquareRepository
import xyz.tetatet.vivaquiz.io.repository.VivaRepository
import xyz.tetatet.vivaquiz.controllers.shared.PreferencesRepository
import xyz.tetatet.vivaquiz.ui.application.BaseApplication
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, NetModule::class, RepositoryModule::class, DbModule::class])
interface ApplicationComponent {

    fun application(): BaseApplication

    fun context(): Context

    fun preferencesRepository(): PreferencesRepository

    /*Title ------------------------> Net Module <------------------------------------*/
    /*Sub --------------------------> API REST Interfaces ----------------------------*/
    @Named("Viva")
    fun vivaApiInterface(): ApiInterface

    @Named("Foursquare")
    fun foursquareApiInterface(): ApiInterface

    /*Title ------------------------> Repository Module <------------------------------*/
    /*Sub --------------------------> Repositories <-----------------------------------*/
    fun vivaRepository(): VivaRepository

    fun foursquareRepository(): FoursquareRepository

    /*Title ------------------------> DataBase Module <-------------------------------------------------------- */
    /*Sub --------------------------- Controllers ------------------------------------*/
    fun databaseController(): DatabaseController

    /*Sub --------------------------- Dbs ------------------------------------*/
    fun productsDatabase(): ProductsDatabase

}