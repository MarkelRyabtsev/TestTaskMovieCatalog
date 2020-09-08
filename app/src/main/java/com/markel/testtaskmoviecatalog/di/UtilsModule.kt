package com.markel.testtaskmoviecatalog.di

import com.google.gson.Gson
import com.markel.testtaskmoviecatalog.utils.RxErrorHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val gsonModule = module {
    single { Gson() }
}

val errorHandleModule = module {
    single(createdAtStart = true) { RxErrorHandler(androidApplication()) }
}