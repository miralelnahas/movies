package com.areeb.domain.models.movies

data class Movie(
    val id: Int,
    val movieId: Int,
    val title: String,
    val imageUrl: String,
    val voteAverage: String,
    val releaseDate: String
)