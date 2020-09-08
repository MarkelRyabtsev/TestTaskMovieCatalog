package com.markel.testtaskmoviecatalog.utils

import androidx.paging.PagedList
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState
import io.reactivex.Observable

data class Listing<T> (

    val pagedList: Observable<PagedList<T>>,
    val refreshState: Observable<NetworkState>? = null,
    val loadMoreState: Observable<NetworkState>? = null,
    val refresh: () -> Unit = {}
)