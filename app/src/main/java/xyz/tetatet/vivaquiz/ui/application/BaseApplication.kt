package xyz.tetatet.vivaquiz.ui.application

import android.app.Application
import com.google.android.libraries.places.api.Places
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.di.application.*

class BaseApplication : Application() {

    var applicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        if (!Places.isInitialized())
            Places.initialize(this, getString(R.string.google_api_key))
    }

    fun getComponent(): ApplicationComponent {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        }
        return applicationComponent!!
    }
    fun clear() {
        applicationComponent = null
    }
}