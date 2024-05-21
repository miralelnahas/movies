package com.areeb.domain.models

import com.areeb.data.network.movies.response.movieDetails.MovieDetailsResponse
import com.areeb.data.network.movies.response.movies.MovieResponse
import com.areeb.domain.models.movieDetails.MovieDetails
import com.areeb.domain.models.movies.Movie
import java.time.LocalDate

object Mapper {
    fun MovieResponse.toMovie(): Movie =
        Movie(
            id,
            id,
            originalTitle,
            imageUrl ?: "",
            String.format("%.1f", voteAverage),
            LocalDate.parse(releaseDate).year.toString()
        )

    fun MovieDetailsResponse.toMovieDetails(): MovieDetails =
        MovieDetails(
            id,
            originalTitle ?: "",
            overview ?: "",
            releaseDate ?: "",
            posterPath ?: "",
            String.format("%.1f", voteAverage),
            runtime ?: 0,
            genres.map { it.name ?: "" },
            productionCountries.map { it.name ?: "" },
            productionCompanies.map { it.name ?: "" },
            spokenLanguages.map { it.englishName ?: "" },
        )

}