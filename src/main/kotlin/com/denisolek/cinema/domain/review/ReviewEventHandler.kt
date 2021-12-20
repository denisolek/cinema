package com.denisolek.cinema.domain.review

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.review.infrastructure.ReviewRepository
import com.denisolek.cinema.domain.review.model.ReviewAdded
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher
import java.math.BigDecimal.valueOf
import java.math.RoundingMode.HALF_UP
import java.time.Instant

class ReviewEventHandler(
    private val repository: ReviewRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun handle(event: ReviewAdded): Either<Failure, Unit> = eager {
        val movieReviews = repository.findAll(MovieId(event.movieId)).bind()
        val rating = movieReviews
            .sumOf { it.stars.value }
            .let {
                val summedStars = valueOf(it.toLong())
                val starsCount = valueOf(movieReviews.size.toLong())
                summedStars.divide(starsCount, 2, HALF_UP)
            }.toDouble()
        SummedReviewChanged(
            movieId = event.movieId,
            date = Instant.now(),
            stars = rating,
            votes = movieReviews.size
        ).let { eventPublisher.publish(it) }
    }
}