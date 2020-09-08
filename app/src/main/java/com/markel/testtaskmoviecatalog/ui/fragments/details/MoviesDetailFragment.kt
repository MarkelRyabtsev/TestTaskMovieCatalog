package com.markel.testtaskmoviecatalog.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.markel.testtaskmoviecatalog.R
import com.markel.testtaskmoviecatalog.databinding.FragmentMoviesDetailBinding
import kotlinx.android.synthetic.main.fragment_movies_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesDetailFragment: Fragment() {

    private val movieViewModel: MoviesDetailViewModel by viewModel()
    private lateinit var binding: FragmentMoviesDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_detail, container, false)
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = movieViewModel
        movieViewModel.fetchMovieDetail(arguments?.getString(FIELD_MOVIE_ID) ?: "")
        button_back_fragmentMoviesDetail.setOnClickListener { activity?.onBackPressed() }
    }

    companion object {
        const val FIELD_MOVIE_ID = "movieId"

        fun newInstance(movieId: String): MoviesDetailFragment {
            return MoviesDetailFragment().apply {
                arguments = bundleOf(FIELD_MOVIE_ID to movieId)
            }
        }
    }
}