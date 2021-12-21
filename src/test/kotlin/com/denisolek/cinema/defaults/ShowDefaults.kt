package com.denisolek.cinema.defaults

import com.denisolek.cinema.domain.show.model.ShowAdded
import com.denisolek.cinema.domain.show.model.ShowPriceUpdated
import com.denisolek.cinema.domain.show.model.ShowTimeUpdated
import java.time.Duration.ofMinutes
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS
import java.util.*

object ShowDefaults {
    val showId = UUID.randomUUID()
    val start = Instant.now().plus(1, DAYS)
    val duration = ofMinutes(90)
    val end = start.plus(duration)
    val price = 11.0
    val currency = "USD"

    val defaultShowAdded = ShowAdded(
        showId = showId,
        movieId = MovieDefaults.movieId.value,
        start = start,
        end = end,
        duration = duration,
        price = price,
        currency = currency
    )

    val defaultShowTimeUpdated = ShowTimeUpdated(
        showId = showId,
        start = start.plus(3, DAYS),
        end = end.plus(3, DAYS),
        duration = duration
    )

    val defaultShowPriceUpdated = ShowPriceUpdated(
        showId = showId,
        price = 15.0,
        currency = currency
    )
}