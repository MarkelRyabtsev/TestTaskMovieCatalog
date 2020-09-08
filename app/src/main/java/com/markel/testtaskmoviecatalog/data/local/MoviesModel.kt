package com.markel.testtaskmoviecatalog.data.local

import com.markel.testtaskmoviecatalog.data.remote.Genre
import com.markel.testtaskmoviecatalog.utils.Api
import com.markel.testtaskmoviecatalog.utils.Consts.Companion.PLACEHOLDER
import com.markel.testtaskmoviecatalog.utils.format
import com.markel.testtaskmoviecatalog.utils.formatHourMinutes

data class MoviesModel (

    val id: String,
    val posterPath: String? = null,
    val title: String? = null,
    val voteAverage: Float? = null,
    val voteCount: Int? = null,
    val overview: String? = null,
    val releaseDate: String? = null,
    val genreList: List<Genre>? = null,
    val runtime: Int? = null
)

fun MoviesModel.getPosterUrl(): String = "${Api.IMAGE_API_ROOT}w500/${this.posterPath}"
fun MoviesModel.displayTitle(): String = this.title ?: PLACEHOLDER
fun MoviesModel.display5StarsRating(): Float = this.voteAverage?.div(2) ?: 0.0f
fun MoviesModel.displayVoteCount(): String = this.voteCount?.format() ?: PLACEHOLDER
fun MoviesModel.displayDuration(): String = this.runtime?.formatHourMinutes() ?: PLACEHOLDER
fun MoviesModel.displayReleaseDate(): String = this.releaseDate ?: PLACEHOLDER
fun MoviesModel.displayOverview(): String = this.overview ?: PLACEHOLDER