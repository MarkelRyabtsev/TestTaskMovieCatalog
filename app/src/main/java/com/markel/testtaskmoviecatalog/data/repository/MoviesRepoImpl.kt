package com.markel.testtaskmoviecatalog.data.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.markel.testtaskmoviecatalog.data.local.GenresModel
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.remote.*
import com.markel.testtaskmoviecatalog.utils.Consts
import com.markel.testtaskmoviecatalog.utils.Listing
import com.markel.testtaskmoviecatalog.utils.NextPageIndex
import com.markel.testtaskmoviecatalog.utils.NextPageIndexImpl
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class MoviesRepoImpl: MoviesRepo, KoinComponent {

    private val movieApi: ApiService by inject()
    private val nextPageIndex: NextPageIndex by lazy { NextPageIndexImpl() }

    override fun getMovieList(category: MoviesCategory, pageSize: Int): Listing<MoviesModel> {

        val dataSourceFactory = MovieListDataSourceFactory(category)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()
        val boundaryCallback = MoviesBoundaryCallback(category, pageSize, nextPageIndex)
        val pagedList = RxPagedListBuilder(dataSourceFactory, pagedListConfig)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()
        return Listing(
            pagedList = pagedList,
            refreshState = boundaryCallback.initLoadState,
            loadMoreState = boundaryCallback.loadMoreState,
            refresh = { boundaryCallback.onZeroItemsLoaded() }
        )
    }

    override fun fetchMovieList(category: MoviesCategory, pageSize: Int): Listing<MoviesModel> {

        val dataSourceFactory = MovieListDataSourceFactory(category)
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(Consts.PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        val pagedList = RxPagedListBuilder(dataSourceFactory, pagedListConfig)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
        return Listing(
            pagedList = pagedList,
            refreshState = dataSourceFactory.initLoadState,
            loadMoreState = dataSourceFactory.loadMoreState,
            refresh = { dataSourceFactory.dataSource?.invalidate() }
        )
    }

    override fun fetchMovieListByFilter(
        title: String,
        genres: List<String>,
        ratingFrom: Int,
        ratingTo: Int,
        pageSize: Int
    ): Listing<MoviesModel> {

        val dataSourceFactory = MovieListByFilterDataSourceFactory(title, genres, ratingFrom, ratingTo)
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(Consts.PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        val pagedList = RxPagedListBuilder(dataSourceFactory, pagedListConfig)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
        return Listing(
            pagedList = pagedList,
            refreshState = dataSourceFactory.initLoadState,
            loadMoreState = dataSourceFactory.loadMoreState,
            refresh = { dataSourceFactory.dataSource?.invalidate() }
        )
    }

    override fun fetchMovieDetail(movieId: String): Single<MoviesModel> {

        return movieApi.fetchMovieDetail(movieId)
            .map { response ->
                MoviesModel(
                    id = response.id,
                    posterPath = response.posterPath,
                    title = response.title,
                    voteAverage = response.voteAverage,
                    voteCount = response.voteCount,
                    overview = response.overview,
                    releaseDate = response.releaseDate,
                    genreList = response.genreList,
                    runtime = response.runtime
                )
            }
    }

    override fun getGenreList(): Single<GenreListResponse> {

        return movieApi.getMovieGenres()
            .map { response ->
                GenreListResponse(
                    results = response.results
                )
            }
    }
}