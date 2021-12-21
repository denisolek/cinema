package com.denisolek.cinema.defaults


import com.denisolek.cinema.defaults.AuthDefaults.moviegoer
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.domain.review.AddReview
import com.denisolek.cinema.domain.review.model.Review
import com.denisolek.cinema.domain.review.model.ReviewAdded
import com.denisolek.cinema.domain.review.model.ReviewUpdated
import com.denisolek.cinema.domain.review.model.Stars.Companion.stars
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.utils.rightValue
import java.time.Instant.now
import java.util.UUID.randomUUID

object ReviewDefaults {
    val reviewId = ReviewId(randomUUID())
    val now = now()
    val stars = stars(4)
    val updatedStars = stars(1)

    val defaultAddReview = AddReview(
        authentication = moviegoer,
        movieId = movieId,
        stars = stars.rightValue.value
    )

    val defaultReview = Review(
        id = reviewId,
        movieId = movieId,
        userId = moviegoer.id,
        date = now(),
        stars = stars.rightValue
    )

    val defaultReviewAdded = ReviewAdded(
        reviewId = reviewId.value,
        movieId = movieId.value,
        userId = moviegoer.id.value,
        date = now,
        stars = stars.rightValue.value,
    )

    val defaultReviewUpdated = ReviewUpdated(
        reviewId = reviewId.value,
        movieId = movieId.value,
        date = now,
        stars = updatedStars.rightValue.value,
    )
}