package xyz.tetatet.vivaquiz.di.main

import dagger.Component
import xyz.tetatet.vivaquiz.di.ViewScope
import xyz.tetatet.vivaquiz.di.application.ActivityModule
import xyz.tetatet.vivaquiz.di.application.ApplicationComponent
import xyz.tetatet.vivaquiz.ui.main.MainActivity

@ViewScope
@Component(dependencies = [ApplicationComponent::class], modules = [MainModule::class, ActivityModule::class])
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}