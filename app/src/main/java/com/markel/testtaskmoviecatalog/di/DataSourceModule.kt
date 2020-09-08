package com.markel.testtaskmoviecatalog.di

import com.markel.testtaskmoviecatalog.data.remote.ApiService
import com.markel.testtaskmoviecatalog.data.repository.MoviesRepo
import com.markel.testtaskmoviecatalog.data.repository.MoviesRepoImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { get<Retrofit>().create(ApiService::class.java) }
}

val repoModule = module {
    single<MoviesRepo> { MoviesRepoImpl() }
}