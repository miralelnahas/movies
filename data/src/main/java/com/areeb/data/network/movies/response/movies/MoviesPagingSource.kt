package com.areeb.data.network.movies.response.movies

import com.areeb.common.SortType
import com.areeb.data.network.RetrofitClientExt.apiCall
import com.areeb.data.network.base.BasePagingSource
import com.areeb.data.network.movies.MoviesApi

class MoviesPagingSource(
    private val moviesApi: MoviesApi,
    private val sortType: SortType = SortType.MOST_POPULAR,
    private val searchQuery: String = ""
) : BasePagingSource<MovieResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        val pageCursor = params.key ?: LIST_STARTING_INDEX
        return try {
            apiCall {
                if (searchQuery.isEmpty()) {
                    when (sortType) {
                        SortType.MOST_POPULAR -> moviesApi.getPopularMovies(
                            pageCursor
                        )

                        SortType.TOP_RATED -> moviesApi.getTopRatedMovies(
                            pageCursor
                        )
                    }
                } else moviesApi.searchMovies(pageCursor, searchQuery)
            }.map {
                response = it.results
            }

            val prevCursor = getPrevCursor(pageCursor)
            val nextCursor = getNextCursor(pageCursor, params.loadSize)

            LoadResult.Page(data = response, prevKey = prevCursor, nextKey = nextCursor)

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}