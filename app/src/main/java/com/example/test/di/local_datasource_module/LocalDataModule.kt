package com.example.test.di.local_datasource_module

import android.app.Application
import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideDao(get()) }
}


fun provideDatabase(application: Context): AppDatabase {
    return Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "prod.database")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
}


fun provideDao(database: AppDatabase): DaoInterface {
    return database.userDao
}