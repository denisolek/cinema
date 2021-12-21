package com.denisolek.cinema.domain.show

import com.denisolek.cinema.domain.authentication.Authentication
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId
import java.math.BigDecimal
import java.time.Instant

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