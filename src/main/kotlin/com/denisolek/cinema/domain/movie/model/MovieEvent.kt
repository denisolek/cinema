package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.movie.MovieData
import com.denisolek.cinema.domain.movie.model.Rating.Companion.rating
import com.denisolek.cinema.domain.movie.model.ReleaseDate.Companion.releaseDate
import com.denisolek.cinema.domain.movie.model.Runtime.Companion.runtime
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class MovieLoaded(
    override val id: UUID = randomUUID(),
    val movieId: String,
    val title: String,
    val description: String,
    val releaseDate: Instant,
    val awards: String,
    val imdbRating: Double,
    val imdbVotes: Int,
    val runtime: Duration
) : DomainEvent {
    companion object {
        fun movieLoaded(movie: MovieData): Either<Failure, MovieLoaded> = eager {
            val imdbRating = rating(movie.imdbRating, movie.imdbVotes).bind()
            MovieLoaded(
                movieId = movie.id,
                title = movie.title,
                description = movie.description,
                releaseDate = releaseDate(movie.releaseDate).bind().value,
                awards = movie.awards,
                imdbRating = imdbRating.rating,
                imdbVotes = imdbRating.votes,
                runtime = runtime(movie.runtime).bind().value
            )
        }
    }
}