package com.markel.testtaskmoviecatalog.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.repository.MoviesRepo
import com.markel.testtaskmoviecatalog.utils.Listing
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesHomeViewModel: ViewModel(), KoinComponent {

    private val moviesRepo: MoviesRepo by inject()
    private val listingMap = mutableMapOf<MoviesCategory, Listing<MoviesModel>>()

    fun fetchList(category: MoviesCategory): Listing<MoviesModel> {
        val listing = moviesRepo.fetchMovieList(category)
        listingMap[category] = listing
        return listing
    }

    fun getList(category: MoviesCategory): Listing<MoviesModel> {
        val listing = moviesRepo.getMovieList(category)
        listingMap[category] = listing
        return listing
    }

    fun refresh() {
        listingMap.values.forEach {
            it.refresh.invoke()
        }
    }

    fun refreshState(): Observable<NetworkState> {
        val stateList = mutableListOf<Observable<NetworkState>>()
        listingMap.values.forEach {
            it.refreshState?.apply {
                stateList.add(this)
            }
        }
        return Observable.combineLatest(stateList) { states ->
            var mergedState = NetworkState.IDLE
            states.forEach {
                val s = it as NetworkState
                if (s == NetworkState.LOADING) {
                    mergedState = NetworkState.ERROR
                }
            }
            mergedState

        }
    }
}