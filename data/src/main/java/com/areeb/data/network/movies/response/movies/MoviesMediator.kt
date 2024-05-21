package com.areeb.data.network.movies.response.movies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.areeb.common.SortType
import com.areeb.data.database.AppDatabase
import com.areeb.data.database.movies.MovieEntity
import com.areeb.data.database.movies.MoviesDao
import com.areeb.data.database.remoteKeys.RemoteKeyEntity
import com.areeb.data.database.remoteKeys.RemoteKeysDao
import com.areeb.data.models.Mapper.toMovieEntity
import com.areeb.data.network.RetrofitClientExt.apiCall
import com.areeb.data.network.base.BasePagingSource
import com.areeb.data.network.movies.MoviesApi

@ExperimentalPagingApi
class MoviesMediator(
    private val moviesApi: MoviesApi,
    private val database: AppDatabase,
    private val remoteKeysDao: RemoteKeysDao,
    private val moviesDao: MoviesDao,
    private val sortType: SortType,
    private val searchQuery: String = ""
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        var response: List<MovieResponse> = listOf()

        val page = when (loadType) {
            LoadType.REFRESH -> BasePagingSource.LIST_STARTING_INDEX

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyEntityForLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKey != null
                    )
                nextKey
            }
        }
        try {
            apiCall {
                if (searchQuery.isEmpty()) {
                    when (sortType) {
                        SortType.MOST_POPULAR -> moviesApi.getPopularMovies(page)
                        SortType.TOP_RATED -> moviesApi.getTopRatedMovies(page)
                    }
                } else moviesApi.searchMovies(page, searchQuery)
            }.map {
                response = it.results
            }

            val endOfPagination = response.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH && response.isNotEmpty()) {
                    remoteKeysDao.clearRemoteKeys()
                    moviesDao.clearAll()
                }
                response.map {
                    moviesDao.insert(it.toMovieEntity(sortType))
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1

                val keys = response.map {
                    RemoteKeyEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insert(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (ex: Exception) {
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movieId ->
                remoteKeysDao.remoteKeysRepoId(movieId.movieId)
            }
    }

    private suspend fun getRemoteKeyEntityForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                remoteKeysDao.remoteKeysRepoId(repo.movieId)
            }
    }

}