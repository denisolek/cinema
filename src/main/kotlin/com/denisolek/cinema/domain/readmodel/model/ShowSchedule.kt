package com.denisolek.cinema.domain.readmodel.model

import com.denisolek.cinema.domain.movie.MovieListingInfo
import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.show.model.ShowAdded
import com.denisolek.cinema.domain.show.model.ShowPriceUpdated
import com.denisolek.cinema.domain.show.model.ShowTimeUpdated
import java.time.Duration
import java.time.Duration.ofMinutes
import java.time.Instant
import java.util.*

data class ShowSchedule(
    val showId: UUID,
    val start: Instant,
    val end: Instant,
    val price: Double,
    val currency: String,
    val movieId: String,
    val movieTitle: String,
    val movieDescription: String,
    val movieRuntime: Duration,
) {
    constructor(event: ShowAdded, movieListingInfo: MovieListingInfo) : this(
        showId = event.showId,
        start = event.start,
        end = event.end,
        price = event.price,
        currency = event.currency,
        movieId = movieListingInfo.id,
        movieTitle = movieListingInfo.title,
        movieDescription = movieListingInfo.description,
        movieRuntime = movieListingInfo.runtime
    )

    fun apply(event: MovieLoaded) = this.copy(
        movieTitle = event.title,
        movieDescription = event.description,
        movieRuntime = ofMinutes(event.runtime)
    )

    fun apply(event: ShowTimeUpdated) = this.copy(
        start = event.start,
        end = event.end
    )

    fun apply(event: ShowPriceUpdated) = this.copy(
        price = event.price,
        currency = event.currency
    )
}