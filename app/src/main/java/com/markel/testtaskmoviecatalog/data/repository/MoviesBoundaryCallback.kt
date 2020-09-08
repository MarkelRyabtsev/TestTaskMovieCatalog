package com.markel.testtaskmoviecatalog.data.repository

import androidx.paging.PagedList
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.remote.ApiService
import com.markel.testtaskmoviecatalog.data.remote.MovieListResponse
import com.markel.testtaskmoviecatalog.data.remote.TMDBApiResponse
import com.markel.testtaskmoviecatalog.utils.NextPageIndex
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesBoundaryCallback(
    private val category: MoviesCategory,
    private val pageSize: Int,
    private val nextPageIndex: NextPageIndex
): PagedList.BoundaryCallback<MoviesModel>(), KoinComponent {

    private val movieApi: ApiService by inject()

    val initLoadState = BehaviorSubject.createDefault(NetworkState.IDLE)
    val loadMoreState = BehaviorSubject.createDefault(NetworkState.IDLE)

    override fun onZeroItemsLoaded() {
        if (initLoadState.value == NetworkState.LOADING) return
        nextPageIndex.setNextPageIndex(category.key, 1)
        movieApi.fetchMovieList(category.key, nextPageIndex.getNextPageIndex(category.key))
    }

    override fun onItemAtEndLoaded(itemAtEnd: MoviesModel) {
        if (loadMoreState.value == NetworkState.LOADING ||
            -1 == nextPageIndex.getNextPageIndex(category.key)) return
        movieApi
            .fetchMovieList(category.key, nextPageIndex.getNextPageIndex(category.key))
    }

    private fun calculateNextPage(totalPage: Int?): Int {
        if (null != totalPage) {
            val currentPage = nextPageIndex.getNextPageIndex(category.key)
            if (currentPage in 1 until totalPage) {
                nextPageIndex.setNextPageIndex(category.key, currentPage.plus(1))
            } else {
                nextPageIndex.setNextPageIndex(category.key, -1)
            }
        }
        return nextPageIndex.getNextPageIndex(category.key)
    }

    private fun changeLoadState(firstLoad: Boolean, state: NetworkState) {
        if (firstLoad) initLoadState.onNext(state)
        else loadMoreState.onNext(state)
    }
}