package com.markel.testtaskmoviecatalog.data.repository

import com.markel.testtaskmoviecatalog.data.local.GenresModel
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.remote.Genre
import com.markel.testtaskmoviecatalog.data.remote.GenreListResponse
import com.markel.testtaskmoviecatalog.utils.Consts
import com.markel.testtaskmoviecatalog.utils.Listing
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import io.reactivex.Single

interface MoviesRepo {

    fun getMovieList(category: MoviesCategory, pageSize: Int = Consts.PAGE_SIZE): Listing<MoviesModel>
    fun fetchMovieList(category: MoviesCategory, pageSize: Int = Consts.PAGE_SIZE): Listing<MoviesModel>
    fun fetchMovieListByFilter(title: String, genres: List<String>, ratingFrom: Int, ratingTo: Int, pageSize: Int = Consts.PAGE_SIZE): Listing<MoviesModel>
    fun fetchMovieDetail(movieId: String): Single<MoviesModel>
    fun getGenreList(): Single<GenreListResponse>
}