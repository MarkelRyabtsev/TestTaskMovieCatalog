package com.markel.testtaskmoviecatalog.ui.fragments.list

import android.content.Context
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.data.local.*
import com.markel.testtaskmoviecatalog.ui.fragments.MoviesClickListener
import kotlin.properties.Delegates.observable

class MovieListController(
    private val context: Context,
    private val clickListener: MoviesClickListener
) : PagedListEpoxyController<MoviesModel>(diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()) {

    private val loadMoreView = LoadMoreView_().apply { id(LoadMoreView::class.java.simpleName) }
    var loadingMore = false
        set(value) {
            field = value
            requestModelBuild()
        }

    var listIsEmpty: Boolean by observable(false) { _, oldValue, newValue ->
        onListChanged?.invoke(oldValue, newValue)
    }

    var onListChanged: ((Boolean, Boolean) -> Unit)? = null

    override fun buildItemModel(currentPosition: Int, item: MoviesModel?): EpoxyModel<*> {
        return item?.run {
            MovieListEpoxyModel_()
                .id(this.id)
                .movieId(this.id)
                .imagePoster(this.getPosterUrl())
                .textTitle(this.displayTitle())
                .rating(this.display5StarsRating())
                .voteCount(context.getString(R.string.vote_count, this.displayVoteCount()))
                .releaseDate(this.displayReleaseDate())
                .itemClickListener { clickListener.onMovieClicked(this.id) }
        } ?: run {
            MovieListEpoxyModel_()
                .id(-currentPosition)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        listIsEmpty = models.isEmpty()
        loadMoreView.addIf(loadingMore, this)
    }
}