package xyz.tetatet.vivaquiz.di.application

import android.app.Activity
import dagger.Module
import dagger.Provides
import xyz.tetatet.vivaquiz.controllers.messages.MessagesController
import xyz.tetatet.vivaquiz.di.ViewScope

@Module
class ActivityModule(private val activity: Activity?) {

    @ViewScope
    @Provides
    fun provideMessagesController(): MessagesController = MessagesController(activity)

}