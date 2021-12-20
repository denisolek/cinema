package com.denisolek.cinema.domain.movie

import java.time.Duration

data class MovieListingInfo(
    val id: String,
    val title: String,
    val description: String
)

data class MovieData(
    val id: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val awards: String,
    val imdbRating: String,
    val imdbVotes: String,
    val runtime: String
)

data class MovieRuntimeInfo(
    val runtime: Duration
)