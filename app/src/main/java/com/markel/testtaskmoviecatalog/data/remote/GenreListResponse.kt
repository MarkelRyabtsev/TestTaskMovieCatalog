package com.markel.testtaskmoviecatalog.data.remote

import com.google.gson.annotations.SerializedName
import com.markel.testtaskmoviecatalog.data.local.GenresModel
import com.markel.testtaskmoviecatalog.data.local.MoviesModel

data class GenreListResponse (

    @SerializedName("genres")
    val results: List<Genre>
)

data class Genre(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

fun List<Genre>.mapToGenresModels(): List<GenresModel> =
    this.map {
        GenresModel(
            id = it.id,
            name = it.name
        )
    }

