package com.areeb.domain.usecases

import androidx.paging.map
import com.areeb.common.SortType
import com.areeb.data.repositories.MoviesRepository
import com.areeb.domain.models.Mapper.toMovie
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(sortType: SortType) =
        moviesRepository.getMovies(sortType).data.map {
            it.map { it.toMovie() }
        }
}