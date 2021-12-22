package com.denisolek.cinema.domain.show.model

import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class ShowAdded(
    override val id: UUID = randomUUID(),
    val showId: UUID,
    val movieId: String,
    val start: Instant,
    val end: Instant,
    val duration: Long,
    val price: Double,
    val currency: String,
) : DomainEvent {
    constructor(show: Show) : this(
        showId = show.id.value,
        movieId = show.movieId.value,
        start = show.showtime.start,
        end = show.showtime.end,
        duration = show.showtime.minutes,
        price = show.price.asDouble,
        currency = show.price.currency.name
    )
}

data class ShowTimeUpdated(
    override val id: UUID = randomUUID(),
    val showId: UUID,
    val start: Instant,
    val end: Instant,
    val duration: Long,
) : DomainEvent {
    constructor(show: Show) : this(
        showId = show.id.value,
        start = show.showtime.start,
        end = show.showtime.end,
        duration = show.showtime.minutes
    )
}

data class ShowPriceUpdated(
    override val id: UUID = randomUUID(),
    val showId: UUID,
    val price: Double,
    val currency: String,
) : DomainEvent {
    constructor(show: Show) : this(
        showId = show.id.value,
        price = show.price.asDouble,
        currency = show.price.currency.name
    )
}

data class ShowRemoved(
    override val id: UUID = randomUUID(),
    val showId: UUID,
) : DomainEvent {
    constructor(showId: ShowId) : this(
        showId = showId.value
    )
}