package com.areeb.data.models

import com.areeb.common.SortType
import com.areeb.data.database.movies.MovieEntity
import com.areeb.data.network.movies.response.movies.MovieResponse

object Mapper {

    fun MovieEntity.toMovieResponse(): MovieResponse =
        MovieResponse(movieId, imageUrl, voteAverage, title, releaseDate)

    fun MovieResponse.toMovieEntity(sortType: SortType): MovieEntity =
        MovieEntity(
            movieId = id,
            title = originalTitle,
            imageUrl = imageUrl ?: "",
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            sortType = sortType
        )
}