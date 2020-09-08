package com.markel.testtaskmoviecatalog.di

import com.markel.testtaskmoviecatalog.utils.Api
import com.markel.testtaskmoviecatalog.utils.ApiInterceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single { ApiInterceptor() }
    single {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(get<ApiInterceptor>())
        builder.protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
        builder.build()
    }

    single<Converter.Factory> { GsonConverterFactory.create() }
    single<CallAdapter.Factory> { RxJava2CallAdapterFactory.create() }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(Api.API_ROOT)
            .addCallAdapterFactory(get())
            .addConverterFactory(get())
            .client(get())
            .build()
    }
}