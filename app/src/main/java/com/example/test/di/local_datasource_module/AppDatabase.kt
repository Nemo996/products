package com.example.test.di.local_datasource_module

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test.data.UserProfile
import com.example.test.data.product_list.Product

@Database(entities = [Product::class, UserProfile::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract val userDao: DaoInterface
}