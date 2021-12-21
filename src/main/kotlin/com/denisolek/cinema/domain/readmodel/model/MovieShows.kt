package com.denisolek.cinema.domain.readmodel.model

import java.time.Duration
import java.time.Instant
import java.util.*

data class MovieShows(
    val movieId: String,
    val title: String,
    val description: String,
    val runtime: Duration,
    val shows: List<MovieShow>
)

data class MovieShow(
    val showId: UUID,
    val start: Instant,
    val end: Instant,
    val price: Double,
    val currency: String
) {
    constructor(schedule: ShowSchedule) : this(
        showId = schedule.showId,
        start = schedule.start,
        end = schedule.end,
        price = schedule.price,
        currency = schedule.currency,
    )
}