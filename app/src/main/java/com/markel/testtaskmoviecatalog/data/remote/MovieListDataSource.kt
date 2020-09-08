package com.markel.testtaskmoviecatalog.data.remote

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieListDataSource(
    private val category: MoviesCategory,
    private val initLoadState: BehaviorSubject<NetworkState>,
    private val loadMoreState: BehaviorSubject<NetworkState>
) : PageKeyedDataSource<Int, MoviesModel>(), KoinComponent {

    private val apiService: ApiService by inject()
    private var currentPage: Int = -1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MoviesModel>
    ) {
        currentPage = 1
        apiService.fetchMovieList(category.key, currentPage)
            .doOnSubscribe { initLoadState.onNext(NetworkState.LOADING) }
            .doOnSuccess {
                it.results?.run {
                    callback.onResult(
                        this.mapToMovieModels(),
                        null,
                        calculateNextPage(it.totalPages)
                    )
                }
                initLoadState.onNext(NetworkState.IDLE)
            }
            .doOnError { initLoadState.onNext(NetworkState.ERROR) }
            .subscribe()
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesModel>) {
        if (-1 == params.key || NetworkState.LOADING == loadMoreState.value) return
        apiService.fetchMovieList(category.key, params.key)
            .doOnSubscribe { loadMoreState.onNext(NetworkState.LOADING) }
            .doOnSuccess {
                it.results?.run {
                    callback.onResult(this.mapToMovieModels(), calculateNextPage(it.totalPages))
                }
                loadMoreState.onNext(NetworkState.IDLE)
            }
            .doOnError { loadMoreState.onNext(NetworkState.ERROR) }
            .subscribe()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MoviesModel>) {}

    private fun calculateNextPage(totalPage: Int?): Int {
        totalPage?.run {
            currentPage = if (currentPage in 1 until totalPage) {
                currentPage.plus(1)
            } else {
                -1
            }
        }
        return currentPage
    }
}

class MovieListDataSourceFactory(private val category: MoviesCategory): DataSource.Factory<Int, MoviesModel>() {

    val initLoadState = BehaviorSubject.createDefault(NetworkState.IDLE)
    val loadMoreState = BehaviorSubject.createDefault(NetworkState.IDLE)
    var dataSource: MovieListDataSource? = null

    override fun create(): DataSource<Int, MoviesModel> {
        dataSource = MovieListDataSource(category, initLoadState, loadMoreState)
        return dataSource!!
    }

}