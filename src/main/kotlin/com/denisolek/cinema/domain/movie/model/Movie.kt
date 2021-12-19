package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import com.denisolek.cinema.domain.movie.infrastructure.MovieData
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId

data class Movie(
    val id: MovieId,
    val title: String,
    val description: String,
    val releaseDate: ReleaseDate,
    val awards: String,
    val ratings: List<Rating>,
    val runtime: Runtime
) {
    companion object {
        fun movie(data: MovieData): Either<Failure, Movie> = TODO()
    }
}