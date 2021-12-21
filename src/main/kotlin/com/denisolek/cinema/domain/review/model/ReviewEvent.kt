package com.denisolek.cinema.domain.review.model

import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class ReviewAdded(
    override val id: UUID = randomUUID(),
    val reviewId: UUID,
    val movieId: String,
    val userId: String,
    val date: Instant,
    val stars: Int
) : DomainEvent {
    constructor(review: Review) : this(
        reviewId = review.id.value,
        movieId = review.movieId.value,
        userId = review.userId.value,
        date = review.date,
        stars = review.stars.value,
    )
}

data class ReviewUpdated(
    override val id: UUID = randomUUID(),
    val reviewId: UUID,
    val movieId: String,
    val date: Instant,
    val stars: Int
) : DomainEvent {
    constructor(review: Review) : this(
        reviewId = review.id.value,
        movieId = review.movieId.value,
        date = review.date,
        stars = review.stars.value
    )
}

data class SummedReviewChanged(
    override val id: UUID = randomUUID(),
    val movieId: String,
    val date: Instant,
    val stars: Double,
    val votes: Int
) : DomainEvent