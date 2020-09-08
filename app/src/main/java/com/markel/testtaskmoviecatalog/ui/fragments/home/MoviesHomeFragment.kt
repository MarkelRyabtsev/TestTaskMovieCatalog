package com.markel.testtaskmoviecatalog.ui.fragments.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.data.local.MoviesModel
import com.markel.testtaskmoviecatalog.ui.fragments.MoviesClickListener
import com.markel.testtaskmoviecatalog.ui.fragments.details.MoviesDetailFragment
import com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers.MoviesCarouselController
import com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers.MoviesHomeController
import com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers.MoviesLargeListController
import com.markel.testtaskmoviecatalog.ui.fragments.home.epoxyControllers.MoviesNormalListController
import com.markel.testtaskmoviecatalog.ui.fragments.home.homeModels.CategoryHeaderHolder
import com.markel.testtaskmoviecatalog.ui.fragments.list.MovieListFragment
import com.markel.testtaskmoviecatalog.ui.fragments.search.MoviesSearchFragment
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.enums.NetworkState
import com.markel.testtaskmoviecatalog.utils.openFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.fragment_movies_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesHomeFragment: Fragment(),
    CategoryHeaderHolder.OnHeaderClickListener,
    MoviesClickListener {

    private val movieViewModel: MoviesHomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryListings = mutableMapOf<MoviesCategory, MoviesCategoryListing>()
        val homeController = MoviesHomeController()

        setupToolbar()

        MoviesCategory.values().filter {
            it != MoviesCategory.BY_FILTER
        }.forEachIndexed { index, category ->

            val carouselController: PagedListEpoxyController<MoviesModel>
            val itemsOnScreen: Float
            if (index == 0) {
                carouselController = MoviesLargeListController(category, this)
                itemsOnScreen = 1.7f
            } else {
                carouselController = MoviesNormalListController(category, this)
                itemsOnScreen = 3.05f
            }
            categoryListings[category] =
                MoviesCategoryListing(
                    this,
                    NetworkState.LOADING,
                    carouselController,
                    itemsOnScreen
                )

            val listing = movieViewModel.getList(category)
            listing.pagedList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { carouselController.submitList(it) }
                .subscribe()
            listing.refreshState
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnNext {
                    categoryListings[category]?.loadingState = it
                    homeController.requestModelBuild()
                }?.subscribe()
            listing.loadMoreState
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnNext {
                    (carouselController as MoviesCarouselController).loadingMore =
                        (it == NetworkState.LOADING)
                }?.subscribe()
        }
        homeController.categoryListings = categoryListings

        with(epoxyRecyclerView_movies_fragmentMoviesHome) {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
            setControllerAndBuildModels(homeController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_movies_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.menuItem_search_menuFragmentMoviesHome){
            activity?.openFragment(MoviesSearchFragment(), true)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        activity?.run {
            (this as AppCompatActivity).setSupportActionBar(toolbar_fragmentMoviesHome)
            this.supportActionBar?.run {
                setDisplayHomeAsUpEnabled(false)
                show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        movieViewModel.refresh()
    }

    override fun onViewAllClicked(category: MoviesCategory) {
        activity?.openFragment(MovieListFragment.newInstance(category), true)
    }

    override fun onMovieClicked(movieId: String) {
        activity?.openFragment(MoviesDetailFragment.newInstance(movieId), true)
    }
}