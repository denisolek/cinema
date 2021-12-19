package com.denisolek.cinema.domain.movie.model

import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.util.*

class MovieLoaded(
    override val id: UUID,
    val movieId: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val awards: String,
    val ratings: List<MovieLoadedRating>,
    val runtime: String
) : DomainEvent {

    class MovieLoadedRating(
        val source: String,
        val rating: String,
        val votes: String
    )

    companion object {
        fun movieLoaded(movie: Movie): MovieLoaded = TODO()
    }
}