package com.markel.testtaskmoviecatalog.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/{list}")
    fun fetchMovieList(
        @Path("list") list: String,
        @Query("page") page: Int? = null
    ): Single<TMDBApiResponse<MovieListResponse>>

    @GET("discover/movie")
    fun fetchMovieListByFilter(
        @Query("with_genres") genresIds: String,
        @Query("vote_average.gte") ratingFrom: Int,
        @Query("vote_average.lte") ratingTo: Int,
        @Query("page") page: Int? = null
    ): Single<TMDBApiResponse<MovieListResponse>>

    @GET("movie/{movieId}")
    fun fetchMovieDetail(@Path("movieId") movieId: String): Single<MovieDetailResponse>

    @GET("genre/movie/list")
    fun getMovieGenres(): Single<GenreListResponse>
}