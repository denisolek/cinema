package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.computations.either
import com.denisolek.cinema.domain.movie.infrastructure.MovieData
import com.denisolek.cinema.domain.movie.model.Rating.Companion.imdbRating
import com.denisolek.cinema.domain.movie.model.Rating.Companion.initialInternalRating
import com.denisolek.cinema.domain.movie.model.ReleaseDate.Companion.releaseDate
import com.denisolek.cinema.domain.movie.model.Runtime.Companion.runtime
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
        fun movie(data: MovieData): Either<Failure, Movie> = either.eager {
            Movie(
                id = MovieId(data.id),
                title = data.title,
                description = data.description,
                releaseDate = releaseDate(data.releaseDate).bind(),
                awards = data.awards,
                ratings = listOf(
                    imdbRating(data.imdbRating, data.imdbVotes).bind(),
                    initialInternalRating.bind()
                ),
                runtime = runtime(data.runtime).bind()
            )
        }
    }
}