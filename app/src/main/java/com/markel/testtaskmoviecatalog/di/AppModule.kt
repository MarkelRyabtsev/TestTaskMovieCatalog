package com.markel.testtaskmoviecatalog.di

import com.markel.testtaskmoviecatalog.ui.fragments.details.MoviesDetailViewModel
import com.markel.testtaskmoviecatalog.ui.fragments.home.MoviesHomeViewModel
import com.markel.testtaskmoviecatalog.ui.fragments.list.MovieListViewModel
import com.markel.testtaskmoviecatalog.ui.fragments.search.MoviesSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
}

val viewModelModule = module {
    viewModel { MoviesHomeViewModel() }
    viewModel { MovieListViewModel() }
    viewModel { MoviesDetailViewModel() }
    viewModel { MoviesSearchViewModel() }
}