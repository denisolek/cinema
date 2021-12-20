package com.denisolek.cinema.domain.show

import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.show.model.Show
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class AddShow(
    val authentication: Authentication,
    val movieId: MovieId,
    val start: Instant,
    val price: BigDecimal
)

data class UpdateShow(
    val authentication: Authentication,
    val showId: ShowId,
    val start: Instant,
    val price: BigDecimal
)

data class RemoveShow(
    val authentication: Authentication,
    val showId: ShowId
)

data class ShowInfo(
    val id: UUID,
    val movieId: String,
    val start: Instant,
    val end: Instant,
    val price: BigDecimal
) {
    constructor(show: Show) : this(
        id = show.id.value,
        movieId = show.movieId.value,
        start = show.showtime.start,
        end = show.showtime.end,
        price = show.price.amount,
    )
}