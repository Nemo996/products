package com.example.test.di.local_datasource_module

import androidx.room.*
import com.example.test.data.product_list.Product
import io.reactivex.Single

@Dao
interface DaoInterface {

   @Query("SELECT * FROM products")
    fun getAllSavedProducts(): Single<MutableList<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: Product)

    @Delete
    fun deteteProduct(product: Product)
}