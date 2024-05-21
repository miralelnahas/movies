package com.areeb.ui.viewmodels.movieDetails

import androidx.lifecycle.SavedStateHandle
import com.areeb.domain.models.movieDetails.MovieDetails
import com.areeb.domain.usecases.GetMovieDetailsUseCase
import com.areeb.domain.usecases.IsNetworkConnectedUseCase
import com.areeb.ui.base.BaseViewModel
import com.areeb.ui.views.movieDetails.MovieDetailsIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    isNetworkConnectedUseCase: IsNetworkConnectedUseCase
) : BaseViewModel<MovieDetailsIntent, MovieDetails>(isNetworkConnectedUseCase) {

    private val movieId: Int? = savedStateHandle[ARG_ID]

    init {
        movieId?.let {
            launchRequest { getMovieDetailsUseCase(it) }
        }
    }

    override fun onTriggerEvent(eventType: MovieDetailsIntent) {
        // There are no events in movie details
    }

    companion object {
        const val ARG_ID = "id"
    }
}