package com.markel.testtaskmoviecatalog.ui.fragments.search

import androidx.lifecycle.ViewModel
import com.markel.testtaskmoviecatalog.data.remote.GenreListResponse
import com.markel.testtaskmoviecatalog.data.repository.MoviesRepo
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesSearchViewModel: ViewModel(), KoinComponent {

    private val movieRepo: MoviesRepo by inject()
    var genres: MutableMap<String, String> = mutableMapOf()

    fun getGenreList(): Single<GenreListResponse> {
        return movieRepo.getGenreList()
    }

    fun getGenreIds(checkedGenreNames: List<String>): List<String>{

        val filtered = genres.filter {
            checkedGenreNames.contains(it.key)
        }.values.toList()

        return filtered
    }
}