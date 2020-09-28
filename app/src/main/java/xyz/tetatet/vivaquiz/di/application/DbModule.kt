package xyz.tetatet.vivaquiz.di.application

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import xyz.tetatet.vivaquiz.db.DatabaseController
import xyz.tetatet.vivaquiz.db.ProductsDatabase
import xyz.tetatet.vivaquiz.ui.application.BaseApplication
import javax.inject.Singleton


@Module
class DbModule {

    @Singleton
    @Provides
    fun provideProductDatabase(context: Context): ProductsDatabase {
        return Room.databaseBuilder(context, ProductsDatabase::class.java, "products.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabaseController(context: Context): DatabaseController = DatabaseController(
        provideProductDatabase(context)
    )
}
