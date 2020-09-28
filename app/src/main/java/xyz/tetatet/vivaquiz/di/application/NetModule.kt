package xyz.tetatet.vivaquiz.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.tetatet.vivaquiz.io.network.ApiClient
import xyz.tetatet.vivaquiz.io.network.ApiInterface
import javax.inject.Named

@Module
class NetModule {

    @Named("Viva")
    @Provides
    fun provideVivaApiInterface(context: Context): ApiInterface =
        ApiClient.getVivaClient(context).create(ApiInterface::class.java)

    @Named("Foursquare")
    @Provides
    fun provideFoursquareApiInterface(context: Context): ApiInterface =
        ApiClient.getFoursquareClient(context).create(ApiInterface::class.java)

}