package com.markel.testtaskmoviecatalog.data.remote

import com.google.gson.annotations.SerializedName

data class TMDBApiResponse<T> (

    @SerializedName("page")
    val page: Int?,
    @SerializedName("total_results")
    val totalResults: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("results")
    val results: List<T>? = null
)