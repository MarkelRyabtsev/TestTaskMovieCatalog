package com.markel.testtaskmoviecatalog.ui.fragments.list

import androidx.lifecycle.ViewModel
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.repository.MoviesRepo
import com.markel.testtaskmoviecatalog.utils.Listing
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieListViewModel: ViewModel(), KoinComponent {

    private val movieRepo: MoviesRepo by inject()

    fun fetchList(category: MoviesCategory): Listing<MoviesModel> {
        return movieRepo.fetchMovieList(category)
    }

    fun fetchListByFilter(title: String, genres: List<String>, ratingFrom: Int, ratingTo: Int): Listing<MoviesModel> {
        return movieRepo.fetchMovieListByFilter(title, genres, ratingFrom, ratingTo)
    }

    fun getList(category: MoviesCategory): Listing<MoviesModel> {
        return movieRepo.getMovieList(category)
    }
}