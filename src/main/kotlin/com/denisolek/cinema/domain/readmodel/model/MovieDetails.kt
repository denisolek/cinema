package com.denisolek.cinema.domain.readmodel.model

import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import java.time.Duration
import java.time.Duration.ofMinutes
import java.time.Instant

data class MovieDetails(
    val id: String,
    val title: String,
    val description: String,
    val releaseDate: Instant,
    val awards: String,
    val internalRating: Double,
    val internalVotes: Int,
    val imdbRating: Double,
    val imdbVotes: Int,
    val runtime: Duration,
) {
    constructor(event: MovieLoaded) : this(
        id = event.movieId,
        title = event.title,
        description = event.description,
        releaseDate = event.releaseDate,
        awards = event.awards,
        internalRating = 0.0,
        internalVotes = 0,
        imdbRating = event.imdbRating,
        imdbVotes = event.imdbVotes,
        runtime = ofMinutes(event.runtime)
    )

    fun apply(event: MovieLoaded) = this.copy(
        title = event.title,
        description = event.description,
        releaseDate = event.releaseDate,
        awards = event.awards,
        imdbRating = event.imdbRating,
        imdbVotes = event.imdbVotes,
        runtime = ofMinutes(event.runtime)
    )

    fun apply(event: SummedReviewChanged) = this.copy(
        internalRating = event.stars,
        internalVotes = event.votes
    )
}