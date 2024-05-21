package com.areeb.data.repositories

import com.areeb.common.SortType
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val moviesDataSource: MoviesDataSource) :
    MoviesRepository {
    override suspend fun getMovies(sortType: SortType) = moviesDataSource.getMovies(sortType)

    override suspend fun searchMovies(searchQuery: String) =
        moviesDataSource.searchMovies(searchQuery)

    override suspend fun getMovieDetails(id: Int) = moviesDataSource.getMovieDetails(id)
}