package com.markel.testtaskmoviecatalog.ui.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.ui.fragments.MoviesClickListener
import com.markel.testtaskmoviecatalog.ui.fragments.details.MoviesDetailFragment
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState
import com.markel.testtaskmoviecatalog.utils.openFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MovieListFragment: Fragment(), MoviesClickListener {

    private val viewModel by sharedViewModel<MovieListViewModel>()
    private val moviesCategory: MoviesCategory by lazy {
        arguments?.getSerializable(FIELD_LIST_CATEGORY) as MoviesCategory
    }
    private val moviesTitle: String by lazy {
        arguments?.getString(FIELD_LIST_TITLE) as String
    }
    private val moviesGenres: ArrayList<String> by lazy {
        arguments?.getStringArrayList(FIELD_LIST_GENRES) as ArrayList<String>
    }
    private val moviesRatingFrom: Int by lazy {
        arguments?.getInt(FIELD_LIST_RATING_FROM) as Int
    }
    private val moviesRatingTo: Int by lazy {
        arguments?.getInt(FIELD_LIST_RATING_TO) as Int
    }
    private lateinit var controller: MovieListController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupList()
        subscribeEmptyListState()
        subscribeDataChangesFromRemote()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onMovieClicked(movieId: String) {
        activity?.openFragment(MoviesDetailFragment.newInstance(movieId), true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        activity?.run {
            (this as AppCompatActivity).setSupportActionBar(toolbar_fragmentMovieList)
            this.supportActionBar?.run {
                setTitle(moviesCategory.strRes)
                setDisplayHomeAsUpEnabled(true)
                show()
            }
        }
    }

    private fun setupList() {
        activity?.let {
            controller = MovieListController(it, this)
        }
        with (epoxyRecyclerView_movies_fragmentMovieList) {
            layoutManager = GridLayoutManager(context, 2)
            setController(controller)
            setItemSpacingRes(R.dimen.size_8)
        }

        subscribeEmptyListState()
    }

    private fun subscribeDataChangesFromRemote() {

        val listing = if (moviesCategory == MoviesCategory.BY_FILTER){
            viewModel.fetchListByFilter(moviesTitle, moviesGenres, moviesRatingFrom, moviesRatingTo)
        } else {
            viewModel.fetchList(moviesCategory)
        }

        subscribePagedList(listing.pagedList)
        listing.loadMoreState?.run { subscribeLoadMoreState(this) }
    }

    private fun subscribePagedList(list: Observable<PagedList<MoviesModel>>) {
        list.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { controller.submitList(it) }
            .subscribe()
    }

    private fun subscribeLoadMoreState(state: Observable<NetworkState>) {
        state.observeOn(AndroidSchedulers.mainThread())
            .doOnNext { controller.loadingMore = (NetworkState.LOADING == it) }
            .subscribe()
    }

    private fun subscribeEmptyListState() {
        controller.onListChanged = { listWasEmpty, listIsEmpty ->
            if (listWasEmpty && listIsEmpty){
                epoxyRecyclerView_movies_fragmentMovieList.visibility = View.GONE
                imageView_notFoundPlaceholder_fragmentMovieList.visibility = View.VISIBLE
            } else {
                epoxyRecyclerView_movies_fragmentMovieList.visibility = View.VISIBLE
                imageView_notFoundPlaceholder_fragmentMovieList.visibility = View.GONE
            }
        }
    }

    companion object {
        const val FIELD_LIST_CATEGORY = "movieListCategory"
        const val FIELD_LIST_TITLE = "filterTitle"
        const val FIELD_LIST_GENRES = "filterGenres"
        const val FIELD_LIST_RATING_FROM = "filterRatingFrom"
        const val FIELD_LIST_RATING_TO = "filterRatingTo"

        fun newInstance(category: MoviesCategory): MovieListFragment = MovieListFragment().apply {
            arguments = bundleOf(FIELD_LIST_CATEGORY to category)
        }

        fun newInstance(category: MoviesCategory, title: String, genres: ArrayList<String>, ratingFrom: Int, ratingTo: Int): MovieListFragment = MovieListFragment().apply {
            arguments = bundleOf(
                FIELD_LIST_CATEGORY to category,
                FIELD_LIST_TITLE to title,
                FIELD_LIST_GENRES to genres,
                FIELD_LIST_RATING_FROM to ratingFrom,
                FIELD_LIST_RATING_TO to ratingTo
            )
        }
    }
}