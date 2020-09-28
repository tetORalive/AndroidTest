package xyz.tetatet.vivaquiz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.tetatet.vivaquiz.io.model.viva.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductsDatabase() : RoomDatabase() {
    abstract fun daoProducts(): DaoProducts
}
