package com.example.test.di.local_datasource_module

import androidx.room.*
import com.example.test.data.UserProfile
import com.example.test.data.product_list.Product
import io.reactivex.Single

@Dao
interface DaoInterface {

   @Query("SELECT * FROM products")
    fun getAllSavedProducts(): Single<MutableList<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("SELECT * FROM users")
    fun getUser(): Single<UserProfile>

    @Query("SELECT * FROM users WHERE username=:username ")
    fun getUserById(username:String): Single<UserProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserProfile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: UserProfile)

    @Delete
    fun deteteUser(User: UserProfile)

}