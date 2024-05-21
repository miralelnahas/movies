package com.areeb.ui.viewmodels.movies

import androidx.lifecycle.viewModelScope
import com.areeb.common.SortType
import com.areeb.domain.models.movies.Movie
import com.areeb.domain.usecases.GetMoviesUseCase
import com.areeb.domain.usecases.IsNetworkConnectedUseCase
import com.areeb.domain.usecases.SearchMoviesUseCase
import com.areeb.ui.base.BasePagingViewModel
import com.areeb.ui.viewmodels.search.SearchUiModel
import com.areeb.ui.views.movies.MovieIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    searchMoviesUseCase: SearchMoviesUseCase,
    isNetworkConnectedUseCase: IsNetworkConnectedUseCase
) : BasePagingViewModel<MovieIntent, Movie>(isNetworkConnectedUseCase) {

    private val _sortType: MutableStateFlow<SortType> = MutableStateFlow(SortType.MOST_POPULAR)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    val searchUiModel = SearchUiModel(viewModelScope, searchMoviesUseCase)

    override fun onTriggerEvent(eventType: MovieIntent) {
        when (eventType) {
            is MovieIntent.LoadMovies -> loadMovies()
            is MovieIntent.ChangeSortType -> changeSortType(eventType.sortType)
        }
    }

    private fun loadMovies() {
        launchPagingRequest {
            getMoviesUseCase(sortType.value)
        }
    }

    private fun changeSortType(sortType: SortType) {
        _sortType.value = sortType
        loadMovies()
    }
}