package com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.ui.fragments.MoviesClickListener
import com.markel.testtaskmoviecatalog.ui.fragments.home.homeModels.*
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory

abstract class MoviesCarouselController(protected val movieCategory: MoviesCategory,
                                        protected val clickListener: MoviesClickListener? = null)
    : PagedListEpoxyController<MoviesModel>() {

    var loadingMore = false
        set(value) {
            field = value
            requestModelBuild()
        }

    private val loadingMoreView =
        HomeLoadMoreView_().apply { id(HomeLoadMoreView::class.java.simpleName) }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        loadingMoreView.addIf(loadingMore && models.isNotEmpty(), this)
    }
}