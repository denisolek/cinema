package com.denisolek.cinema.domain.movie.model

import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.util.*

data class MovieLoaded(
    override val id: UUID,
    val movieId: String,
    val title: String,
    val description: String,
    val releaseDate: Instant,
    val awards: String,
    val ratings: List<MovieLoadedRating>,
    val runtime: Duration
) : DomainEvent {

    data class MovieLoadedRating(val source: String, val rating: BigDecimal, val votes: Long) {
        constructor(rating: Rating) : this(rating.source.name, rating.rating, rating.votes)
    }

    companion object {
        fun movieLoaded(movie: Movie): MovieLoaded = MovieLoaded(
            id = UUID.randomUUID(),
            movieId = movie.id.value,
            title = movie.title,
            description = movie.description,
            releaseDate = movie.releaseDate.value,
            awards = movie.awards,
            ratings = movie.ratings.map {
                MovieLoadedRating(
                    source = it.source.name,
                    rating = it.rating,
                    votes = it.votes
                )
            },
            runtime = movie.runtime.value
        )
    }
}