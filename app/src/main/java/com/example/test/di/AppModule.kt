package com.example.test.di

import android.content.Context
import android.content.SharedPreferences
import com.example.test.di.local_datasource_module.databaseModule
import com.example.test.di.network_module.remoteDataSourceModule
import com.example.test.manager.DataManager
import com.example.test.view_model.MyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel { MyViewModel(get(),get()) }

    single { DataManager(androidContext(),get(),get()) }
    single<SharedPreferences> { androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE) }
}

val allModules = listOf(appModule,remoteDataSourceModule,databaseModule)