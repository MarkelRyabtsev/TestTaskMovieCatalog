package com.markel.testtaskmoviecatalog.ui.fragments.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.local.*
import com.markel.testtaskmoviecatalog.data.repository.MoviesRepo
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesDetailViewModel: ViewModel(), KoinComponent {

    private val movieRepo: MoviesRepo by inject()
    private val _movieDetail = MutableLiveData<MoviesModel>()

    val posterUrl: LiveData<String> = Transformations.map(_movieDetail) { it.getPosterUrl() }
    val title: LiveData<String> = Transformations.map(_movieDetail) { it.displayTitle() }
    val rating: LiveData<Float> = Transformations.map(_movieDetail) { it.display5StarsRating() }
    val voteCount: LiveData<String> = Transformations.map(_movieDetail) { it.displayVoteCount() }
    val duration: LiveData<String> = Transformations.map(_movieDetail) { it.displayDuration() }
    val releaseDate: LiveData<String> = Transformations.map(_movieDetail) { it.displayReleaseDate() }
    val overview: LiveData<String> = Transformations.map(_movieDetail) { it.displayOverview() }

    fun fetchMovieDetail(id: String) {
        movieRepo.fetchMovieDetail(id)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { _movieDetail.postValue(it) }
            .subscribe()
    }
}