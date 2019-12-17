package com.example.test

import android.app.Application
import com.example.test.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(allModules)
        }
    }
}