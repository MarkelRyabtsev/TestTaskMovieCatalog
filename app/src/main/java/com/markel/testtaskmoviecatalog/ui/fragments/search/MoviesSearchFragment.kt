package com.markel.testtaskmoviecatalog.ui.fragments.search

import android.os.Bundle
import android.view.*
import android.view.View.NO_ID
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.ui.fragments.list.MovieListFragment
import com.markel.testtaskmoviecatalog.utils.enums.MoviesCategory
import com.markel.testtaskmoviecatalog.utils.openFragment
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movies_search.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesSearchFragment: Fragment() {

    private val viewModel by sharedViewModel<MoviesSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movies_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupGenresChips()
        setupListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
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
            (this as AppCompatActivity).setSupportActionBar(toolbar_fragmentMoviesSearch)
            this.supportActionBar?.run {
                setTitle(getString(R.string.filters))
                setDisplayHomeAsUpEnabled(true)
                show()
            }
        }
    }

    private fun setupGenresChips() {
        viewModel.getGenreList()
            .subscribeOn(Schedulers.newThread())
            .doOnSuccess{ response ->
                response.results.forEach{
                    createChipGenre(it.name)
                    viewModel.genres[it.name] = it.id
                }
            }
            .subscribe()
    }

    private fun createChipGenre(chipText: String){
        activity?.runOnUiThread {
            val chip = layoutInflater.inflate(
                R.layout.chip_genre,
                chipGroup_genres_fragmentMoviesSearch,
                false
            ) as Chip
            chip.text = chipText
            chip.id = NO_ID
            chipGroup_genres_fragmentMoviesSearch.addView(chip)
        }
    }

    private fun setupListeners(){
        button_showByFilter_fragmentMoviesSearch?.setOnClickListener{
            searchByFilters()
        }
    }

    private fun searchByFilters(){
        //val filterTitle = editText_title_fragmentMoviesSearch.text.toString()
        val filterGenres = ArrayList(getCheckedChipsIds())
        val filterRatingFrom = rangeSlider_rating_fragmentMoviesSearch.values[0].toInt()
        val filterRatingTo = rangeSlider_rating_fragmentMoviesSearch.values[1].toInt()
        val category = MoviesCategory.BY_FILTER
        activity?.openFragment(MovieListFragment.newInstance(category, "", filterGenres, filterRatingFrom, filterRatingTo), true)
    }

    private fun getCheckedChipsIds(): List<String> {

        val checkedChips: MutableList<String> = mutableListOf()
        for (i in 0 until chipGroup_genres_fragmentMoviesSearch.childCount) {
            val chip = chipGroup_genres_fragmentMoviesSearch.getChildAt(i) as Chip
            if (chip.isChecked) {
                checkedChips.add(chip.text.toString())
            }
        }

        return viewModel.getGenreIds(checkedChips)
    }
}