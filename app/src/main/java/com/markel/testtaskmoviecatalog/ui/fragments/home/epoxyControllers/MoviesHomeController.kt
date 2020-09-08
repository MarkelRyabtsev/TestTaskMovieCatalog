package com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers

import com.airbnb.epoxy.EpoxyController
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.ui.fragments.home.MoviesCategoryListing
import com.markel.testtaskmoviecatalog.ui.fragments.home.homeModels.*
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState

class MoviesHomeController : EpoxyController() {

    var categoryListings: Map<MoviesCategory, MoviesCategoryListing>? = null

    override fun buildModels() {
        categoryListings?.forEach { category, listing ->
            CategoryHeaderHolder_()
                .id("${category.key}-header")
                .category(category)
                .clickListener(listing.headerClickListener)
                .addTo(this)
            if (listing.loadingState == NetworkState.LOADING) {
                HomeLoadInitView_()
                    .id("${category.key}-loading")
                    .addTo(this)
            } else {
                MoviesCarouselModel_()
                    .id("${category.key}-list")
                    .models(emptyList()) // this is required
                    .epoxyController(listing.carouselController)
                    .paddingRes(R.dimen.size_8)
                    .numViewsToShowOnScreen(listing.itemCountOnScreen)
                    .addTo(this)
            }
        }
    }
}