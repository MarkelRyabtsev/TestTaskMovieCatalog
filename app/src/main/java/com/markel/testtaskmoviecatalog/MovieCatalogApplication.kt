package com.markel.testtaskmoviecatalog

import android.app.Application
import com.markel.testtaskmoviecatalog.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieCatalogApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieCatalogApplication)
            val dependencies = mutableListOf(
                gsonModule,
                errorHandleModule,
                networkModule,
                appModule,
                viewModelModule,
                apiModule,
                repoModule)
            modules(dependencies)
        }
    }
}