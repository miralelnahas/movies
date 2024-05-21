package com.areeb.ui.views.movies

import android.content.Context
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.areeb.common.SortType
import com.areeb.domain.models.DataState
import com.areeb.domain.models.movies.Movie
import com.areeb.ui.MainActivity
import com.areeb.ui.R
import com.areeb.ui.base.BasePagingFragment
import com.areeb.ui.databinding.FragmentMoviesBinding
import com.areeb.ui.utils.Extensions.observe
import com.areeb.ui.viewmodels.movies.MoviesViewModel
import com.google.android.material.search.SearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment :
    BasePagingFragment<FragmentMoviesBinding, MovieIntent, Movie>(R.layout.fragment_movies) {

    override val vm: MoviesViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var searchAdapter: MoviesAdapter

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            vb.layoutMovies.searchView.let {
                if (it.isShowing) {
                    it.hide()
                } else {
                    remove()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).handleBackPressed(backPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback.remove()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sendIntent(MovieIntent.LoadMovies)
        sendSearchIntent(SearchIntent.SearchMovies)
    }

    private fun sendSearchIntent(intent: SearchIntent) {
        lifecycleScope.launch {
            vm.searchUiModel.dataIntentChannel.send(intent)
        }
    }

    override fun initViews() {
        super.initViews()
        initSearchViews()
        initMoviesViews()
    }

    override fun setupObservers() {
        super.setupObservers()
        observe(vm.dataState) {
            onMoviesStateChanged(it)
        }
        observe(vm.searchUiModel.dataState) {
            onSearchStateChanged(it)
        }
        observe(vm.sortType) {
            updateMenuItemIcon(it)
        }
    }

    private fun initSearchViews() {
        searchAdapter = MoviesAdapter { movie ->
            navigateTo(MoviesFragmentDirections.actionMoviesToMovieDetails(movie.movieId))
        }
        vb.layoutMovies.apply {
            rvMoviesSearch.apply {
                adapter = searchAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
            searchView.editText.addTextChangedListener { text ->
                sendSearchIntent(SearchIntent.UpdateSearchQuery(text.toString()))
            }
        }
        searchAdapter.addLoadStateListener {
            if (it.refresh is LoadState.NotLoading && it.prepend.endOfPaginationReached && !it.append.endOfPaginationReached) {
                vb.layoutMovies.rvMoviesSearch.scrollToPosition(0)
            }
        }
    }

    private fun initMoviesViews() {
        moviesAdapter = MoviesAdapter { movie ->
            navigateTo(MoviesFragmentDirections.actionMoviesToMovieDetails(movie.movieId))
        }
        vb.layoutMovies.apply {
            rvMovies.apply {
                adapter = moviesAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
            handleMenuItemClick(appBar)
        }
        moviesAdapter.addLoadStateListener {
            if ((it.refresh is LoadState.NotLoading && it.prepend.endOfPaginationReached)
                || it.mediator?.prepend?.endOfPaginationReached == true
            ) {
                stopLoader()
            }
        }
    }

    private fun handleMenuItemClick(appBar: SearchBar) {
        appBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.most_popular -> onMenuItemClick(SortType.MOST_POPULAR)

                R.id.top_rated -> onMenuItemClick(SortType.TOP_RATED)
            }
            false
        }
    }

    private fun onMenuItemClick(sortType: SortType) {
        sendIntent(MovieIntent.ChangeSortType(sortType))
    }

    private fun updateMenuItemIcon(selectedSortType: SortType) {
        val sortMenu = vb.layoutMovies.appBar.menu.findItem(R.id.sort).subMenu
        sortMenu?.forEach { sortMenuItem ->
            sortMenuItem.icon = ContextCompat.getDrawable(
                requireContext(),
                if (isMenuItemEqualsSortType(selectedSortType, sortMenuItem))
                    R.drawable.ic_radio_button_selected
                else R.drawable.ic_radio_button_unselected
            )
        }
    }

    private fun isMenuItemEqualsSortType(selectedSortType: SortType, sortMenuItem: MenuItem) =
        (selectedSortType == SortType.MOST_POPULAR && sortMenuItem.itemId == R.id.most_popular)
                || (selectedSortType == SortType.TOP_RATED && sortMenuItem.itemId == R.id.top_rated)

    private fun onMoviesStateChanged(state: DataState<PagingData<Movie>>) {
        when (state) {
            DataState.Loading -> {
                startLoader()
            }

            is DataState.Success -> {
                moviesAdapter.submitData(viewLifecycleOwner.lifecycle, state.data)
            }

            is DataState.Error -> {
                stopLoader()
            }

            else -> {

            }
        }
    }

    private fun onSearchStateChanged(state: DataState<PagingData<Movie>>) {
        when (state) {
            is DataState.Success -> {
                searchAdapter.submitData(viewLifecycleOwner.lifecycle, state.data)
            }

            else -> {}
        }
    }

    private fun startLoader() {
        vb.layoutLoader.shimmerContainer.apply {
            post {
                visibility = VISIBLE
                startShimmer()
            }
        }
    }

    private fun stopLoader() {
        vb.layoutLoader.shimmerContainer.apply {
            post {
                visibility = GONE
                stopShimmer()
            }
        }
    }
}