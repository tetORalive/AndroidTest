package xyz.tetatet.vivaquiz.db

import androidx.room.Dao
import androidx.room.Insert


import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import xyz.tetatet.vivaquiz.io.model.viva.Product

@Dao
interface DaoProducts {

    @Insert(onConflict = REPLACE)
    fun insertMultipleRecord(vararg product: Product)

    @Insert(onConflict = REPLACE)
    fun insertMultipleListRecord(product: MutableList<Product?>)

    @Insert(onConflict = REPLACE)
    fun insertOnlySingleRecord(product: Product)

    @Update(onConflict = REPLACE)
    fun updateRecord(product: Product?)

    @Delete
    fun deleteRecord(product: Product)

    @Query("DELETE FROM Product")
    fun deleteAllRecords()

    @Query("SELECT * FROM Product")
    fun fetchAllData(): MutableList<Product?>

    @Query("SELECT COUNT(*) FROM Product")
    fun count(): Int
}
