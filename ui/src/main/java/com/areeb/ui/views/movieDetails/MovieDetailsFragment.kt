package com.areeb.ui.views.movieDetails

import androidx.fragment.app.viewModels
import com.areeb.domain.models.DataState
import com.areeb.domain.models.movieDetails.MovieDetails
import com.areeb.ui.R
import com.areeb.ui.base.BaseFragment
import com.areeb.ui.databinding.FragmentMovieDetailsBinding
import com.areeb.ui.utils.Extensions.observe
import com.areeb.ui.viewmodels.movieDetails.MovieDetailsViewModel
import com.areeb.ui.views.custom.MovieDetailsHeaderInfo.Companion.setValue
import com.areeb.ui.views.custom.MovieDetailsRow.Companion.setValue
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding, MovieDetailsIntent, MovieDetails>(R.layout.fragment_movie_details) {
    override val vm: MovieDetailsViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        vb.layoutMovieDetails.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        observe(vm.dataState) {
            when (it) {
                DataState.Loading -> {
                    startLoader()
                }

                is DataState.Success -> {
                    stopLoader()
                    setViewValues(it.data)
                }

                is DataState.Error -> {
                    stopLoader()
                }

                else -> {

                }
            }
        }
    }

    private fun setViewValues(movieDetails: MovieDetails) {
        vb.layoutMovieDetails.apply {
            layoutGenres.setValue(movieDetails.genres.joinToString(", "))
            layoutProductionCompanies.setValue(movieDetails.productionCompanies.joinToString(", "))
            layoutProductionCountries.setValue(movieDetails.productionCountries.joinToString(", "))
            layoutSpokenLanguages.setValue(movieDetails.spokenLanguages.joinToString(", "))
            layoutDuration.setValue(movieDetails.duration.let { duration ->
                val hours = duration / 60
                val minutes = duration % 60
                val hourString = if (hours > 0) "${hours}h " else ""
                val minuteString = "${minutes}m"
                "$hourString $minuteString"
            })
            layoutRating.setValue(movieDetails.voteAverage)
            layoutReleaseDate.setValue(movieDetails.releaseDate.let { releaseDate ->
                LocalDate.parse(releaseDate).year.toString()
            })
        }
    }

    private fun startLoader() {
        vb.layoutLoader.container.apply {
            post {
                startShimmer()
            }
        }
    }

    private fun stopLoader() {
        vb.layoutLoader.container.apply {
            post {
                stopShimmer()
            }
        }
    }
}