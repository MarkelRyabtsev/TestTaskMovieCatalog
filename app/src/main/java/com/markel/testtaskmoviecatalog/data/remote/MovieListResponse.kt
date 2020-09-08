package com.markel.testtaskmoviecatalog.data.remote

import com.google.gson.annotations.SerializedName
import com.markel.testtaskmoviecatalog.data.local.MoviesModel

data class MovieListResponse (

    @SerializedName("id")
    val id: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("release_date")
    val releaseDate: String?
)

fun List<MovieListResponse>.mapToMovieModels(): List<MoviesModel> =
    this.map {
        MoviesModel(
            id = it.id,
            posterPath = it.posterPath,
            title = it.title,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount,
            releaseDate = it.releaseDate
        )
    }

data class MovieDetailResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("genres")
    val genreList: List<Genre>?,
    @SerializedName("runtime")
    val runtime: Int?
)