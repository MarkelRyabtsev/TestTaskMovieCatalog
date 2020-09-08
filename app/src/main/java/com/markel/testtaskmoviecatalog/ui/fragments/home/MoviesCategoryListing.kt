package com.markel.testtaskmoviecatalog.ui.fragments.home

import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.ui.fragments.home.homeModels.CategoryHeaderHolder
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState

data class MoviesCategoryListing(
    val headerClickListener: CategoryHeaderHolder.OnHeaderClickListener? = null,
    var loadingState: NetworkState? = null,
    val carouselController: PagedListEpoxyController<MoviesModel>,
    val itemCountOnScreen: Float = 0.0f
)