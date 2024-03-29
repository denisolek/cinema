package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.computations.either
import com.denisolek.cinema.domain.movie.MovieData
import com.denisolek.cinema.domain.movie.model.Runtime.Companion.runtime
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId

data class Movie(
    val id: MovieId,
    val title: String,
    val description: String,
    val runtime: Runtime
) {
    companion object {
        fun movie(data: MovieData): Either<Failure, Movie> = either.eager {
            Movie(
                id = MovieId(data.id),
                title = data.title,
                description = data.description,
                runtime = runtime(data.runtime).bind()
            )
        }
    }
}