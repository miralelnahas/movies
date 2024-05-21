package com.areeb.domain.usecases

import androidx.paging.map
import com.areeb.data.repositories.MoviesRepository
import com.areeb.domain.models.Mapper.toMovie
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(searchQuery: String) =
        moviesRepository.searchMovies(searchQuery).data.map {
            it.map {
                it.toMovie()
            }
        }
}