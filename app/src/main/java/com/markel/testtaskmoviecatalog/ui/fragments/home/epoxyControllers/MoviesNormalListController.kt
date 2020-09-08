package com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers

import com.airbnb.epoxy.EpoxyModel
import com.markel.testtaskmoviecatalog.MovieLargeBindingModel_
import com.markel.testtaskmoviecatalog.MovieNormalBindingModel_
import com.markel.testtaskmoviecatalog.data.local.*
import com.markel.testtaskmoviecatalog.ui.fragments.MoviesClickListener
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory

class MoviesNormalListController(
    movieCategory: MoviesCategory,
    clickListener: MoviesClickListener? = null
): MoviesCarouselController(movieCategory, clickListener) {
    override fun buildItemModel(currentPosition: Int, item: MoviesModel?): EpoxyModel<*> {
        return item?.run {
            MovieNormalBindingModel_()
                .id("${movieCategory}${this.id}")
                .movieId(this.id)
                .posterImage(this.getPosterUrl())
                .title(this.displayTitle())
                .rating(this.voteAverage)
                .clickListener(clickListener)
        } ?: run {
            MovieLargeBindingModel_()
                .id(-currentPosition)
        }
    }
}