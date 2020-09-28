package xyz.tetatet.vivaquiz.di.application

import dagger.Module
import dagger.Provides
import xyz.tetatet.vivaquiz.io.network.ApiInterface
import xyz.tetatet.vivaquiz.io.repository.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesVivaApiRepository
                (@Named("Viva") apiInterface: ApiInterface): VivaRepository =
        VivaRepositoryImpl(apiInterface)

    @Singleton
    @Provides
    fun providesFoursquareApiRepository
                (@Named("Foursquare") apiInterface: ApiInterface): FoursquareRepository =
        FoursquareRepositoryImpl(apiInterface)

}