package com.denisolek.cinema.domain.review.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.review.model.Review
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.domain.shared.UserId

interface ReviewRepository {
    fun save(review: Review): Either<IOError, Unit>
    fun find(reviewId: ReviewId): Either<IOError, Review>
    fun find(userId: UserId, movieId: MovieId): Either<IOError, Review>
    fun findAll(movieId: MovieId): Either<IOError, List<Review>>
}