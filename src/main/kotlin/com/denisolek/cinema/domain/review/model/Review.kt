package com.denisolek.cinema.domain.review.model

import arrow.core.Either
import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.review.AddReview
import com.denisolek.cinema.domain.review.model.Stars.Companion.stars
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.domain.shared.UserId
import com.denisolek.cinema.domain.shared.event.DomainEvent
import java.time.Instant
import java.time.Instant.now

data class Review(
    val id: ReviewId,
    val movieId: MovieId,
    val userId: UserId,
    val date: Instant,
    val stars: Stars,
) {
    fun update(command: AddReview): Either<Failure, ReviewOperation> = eager {
        val newStars = stars(command.stars).bind()
        if (stars.differsFrom(newStars)) {
            this@Review.copy(
                date = now(),
                stars = newStars
            ).let {
                return@eager ReviewOperation(it, listOf(ReviewUpdated(it)))
            }
        } else ReviewOperation(this@Review)
    }

    companion object {
        fun review(command: AddReview): Either<Failure, ReviewOperation> = eager {
            Review(
                id = ReviewId(),
                movieId = command.movieId,
                userId = command.authentication.id,
                date = now(),
                stars = stars(command.stars).bind()
            ).let { ReviewOperation(it, listOf(ReviewAdded(it))) }
        }
    }
}

data class ReviewOperation(val review: Review, val events: List<DomainEvent> = listOf())