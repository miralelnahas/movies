package com.areeb.data.repositories

import androidx.paging.PagingData
import com.areeb.data.models.PagingResponse
import com.areeb.common.SortType
import com.areeb.data.network.movies.response.movieDetails.MovieDetailsResponse
import com.areeb.data.network.movies.response.movies.MovieResponse

interface MoviesRepository {
    suspend fun getMovies(sortType: SortType): PagingResponse<PagingData<MovieResponse>>
    suspend fun searchMovies(searchQuery: String): PagingResponse<PagingData<MovieResponse>>
    suspend fun getMovieDetails(id: Int): Result<MovieDetailsResponse>
}