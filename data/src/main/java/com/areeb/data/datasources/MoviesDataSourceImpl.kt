package com.areeb.data.datasources

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.areeb.common.SortType
import com.areeb.data.database.AppDatabase
import com.areeb.data.database.movies.MoviesDao
import com.areeb.data.database.remoteKeys.RemoteKeysDao
import com.areeb.data.models.Mapper.toMovieResponse
import com.areeb.data.models.PagingResponse
import com.areeb.data.network.RetrofitClientExt.apiCall
import com.areeb.data.network.base.BasePagingSource
import com.areeb.data.network.movies.MoviesApi
import com.areeb.data.network.movies.response.movieDetails.MovieDetailsResponse
import com.areeb.data.network.movies.response.movies.MovieResponse
import com.areeb.data.network.movies.response.movies.MoviesMediator
import com.areeb.data.network.movies.response.movies.MoviesPagingSource
import com.areeb.data.repositories.MoviesDataSource
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

class MoviesDataSourceImpl @Inject constructor(
    retrofit: Retrofit, private val database: AppDatabase, private val remoteKeysDao: RemoteKeysDao,
    private val moviesDao: MoviesDao
) : MoviesDataSource {
    private val moviesApi = retrofit.create(MoviesApi::class.java)

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(sortType: SortType): PagingResponse<PagingData<MovieResponse>> {
        val remoteMediator = MoviesMediator(moviesApi, database, remoteKeysDao, moviesDao, sortType)
        return fetchMoviesWithCaching(remoteMediator, sortType)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun fetchMoviesWithCaching(
        remoteMediator: MoviesMediator, sortType: SortType
    ): PagingResponse<PagingData<MovieResponse>> {
        return PagingResponse(Pager(
            PagingConfig(
                pageSize = BasePagingSource.LIST_PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = BasePagingSource.LIST_PAGE_SIZE
            ), remoteMediator = remoteMediator
        ) {
            when (sortType) {
                SortType.MOST_POPULAR -> database.movieEntityDao().getPopularMovies()

                SortType.TOP_RATED -> database.movieEntityDao().getTopRatedMovies()
            }
        }.flow.map {
            it.map { movieEntity ->
                movieEntity.toMovieResponse()
            }
        })
    }

    override suspend fun searchMovies(searchQuery: String): PagingResponse<PagingData<MovieResponse>> {
        val pagingSource = MoviesPagingSource(moviesApi, searchQuery = searchQuery)
        return searchMovies(pagingSource)
    }

    private fun searchMovies(pagingSource: MoviesPagingSource): PagingResponse<PagingData<MovieResponse>> {
        return PagingResponse(
            Pager(
                PagingConfig(
                    pageSize = BasePagingSource.LIST_PAGE_SIZE,
                    enablePlaceholders = false,
                    initialLoadSize = BasePagingSource.LIST_PAGE_SIZE
                )
            ) {
                pagingSource
            }.flow
        )
    }


    override suspend fun getMovieDetails(id: Int): Result<MovieDetailsResponse> {
        return apiCall {
            moviesApi.getMovieDetails(id)
        }
    }
}