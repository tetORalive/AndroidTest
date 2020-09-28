package xyz.tetatet.vivaquiz.di.application

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.tetatet.vivaquiz.controllers.shared.PreferencesRepository
import xyz.tetatet.vivaquiz.ui.application.BaseApplication

import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApplication: BaseApplication) {

    @Singleton
    @Provides
    fun provideApplication(): BaseApplication = baseApplication

    @Singleton
    @Provides
    fun provideContext(): Context = baseApplication.applicationContext

    @Singleton
    @Provides
    fun providePreferencesRepository(context: Context): PreferencesRepository = PreferencesRepository(context)

}