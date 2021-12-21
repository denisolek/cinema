package com.denisolek.cinema.domain.movie

import com.denisolek.cinema.domain.movie.model.Movie
import java.time.Duration

data class MovieListingInfo(
    val id: String,
    val title: String,
    val description: String,
    val runtime: Duration
) {
    constructor(movie: Movie) : this(
        id = movie.id.value,
        title = movie.title,
        description = movie.description,
        runtime = movie.runtime.value
    )
}

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