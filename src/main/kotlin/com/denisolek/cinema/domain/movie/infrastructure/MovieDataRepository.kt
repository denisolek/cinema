package com.denisolek.cinema.domain.movie.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId

interface MovieDataRepository {
    fun find(movieIds: List<MovieId>): Either<IOError, List<MovieData>>
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