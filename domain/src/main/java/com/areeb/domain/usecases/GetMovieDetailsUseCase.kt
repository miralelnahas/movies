package com.areeb.domain.usecases

import com.areeb.data.repositories.MoviesRepository
import com.areeb.domain.models.Mapper.toMovieDetails
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(id: Int) = moviesRepository.getMovieDetails(id).mapCatching {
        it.toMovieDetails()
    }
}