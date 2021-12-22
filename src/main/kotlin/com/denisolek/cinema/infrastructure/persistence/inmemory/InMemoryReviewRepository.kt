package com.denisolek.cinema.infrastructure.persistence.inmemory

import arrow.core.Either
import arrow.core.computations.either.eager
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.review.infrastructure.ReviewRepository
import com.denisolek.cinema.domain.review.model.Review
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.DataIntegrityViolation
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.domain.shared.UserId
import mu.KotlinLogging.logger

class InMemoryReviewRepository : ReviewRepository {
    private val log = logger {}
    private val reviews: MutableMap<ReviewId, Review> = mutableMapOf()

    override fun save(review: Review): Either<IOError, Unit> = eager {
        log.info { "Saving $review" }
        reviews[review.id] = review
    }

    override fun find(reviewId: ReviewId): Either<IOError, Review> {
        return reviews[reviewId]?.right() ?: NotFound("${reviewId.value}").left()
    }

    override fun find(userId: UserId, movieId: MovieId): Either<IOError, Review> =
        reviews.values
            .filter { it.userId == userId && it.movieId == movieId }
            .let {
                when {
                    it.size == 1 -> it.first().right()
                    it.isEmpty() -> NotFound("$userId, $movieId").left()
                    else -> {
                        log.error { "Found ${it.size} reviews for $userId, $movieId" }
                        DataIntegrityViolation("$userId, $movieId").left()
                    }
                }
            }

    override fun findAll(movieId: MovieId): Either<IOError, List<Review>> =
        reviews.values.filter { it.movieId == movieId }.right()

    fun clear() = reviews.clear()
}
