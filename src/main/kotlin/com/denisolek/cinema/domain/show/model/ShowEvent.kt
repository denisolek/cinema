package com.denisolek.cinema.domain.show.model

import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class ShowAdded(
    override val id: UUID = randomUUID(),
    val showId: UUID,
    val movieId: String,
    val start: Instant,
    val duration: Duration,
    val price: BigDecimal,
    val currency: String
) : DomainEvent {
    constructor(show: Show) : this(
        showId = show.id.value,
        movieId = show.movieId.value,
        start = show.showtime.start,
        duration = show.showtime.duration,
        price = show.price.amount,
        currency = show.price.currency.name
    )
}

data class ShowTimeUpdated(
    override val id: UUID = randomUUID(),
    val showId: UUID,
    val start: Instant,
    val duration: Duration
) : DomainEvent {
    constructor(show: Show) : this(
        showId = show.id.value,
        start = show.showtime.start,
        duration = show.showtime.duration
    )
}

data class ShowPriceUpdated(
    override val id: UUID = randomUUID(),
    val showId: UUID,
    val price: BigDecimal,
    val currency: String
) : DomainEvent {
    constructor(show: Show) : this(
        showId = show.id.value,
        price = show.price.amount,
        currency = show.price.currency.name
    )
}

data class ShowRemoved(
    override val id: UUID = randomUUID(),
    val showId: UUID
) : DomainEvent {
    constructor(showId: ShowId) : this(
        showId = showId.value
    )
}