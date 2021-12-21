package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.shared.ValidationError.RatingValidationError
import com.denisolek.cinema.domain.shared.ValidationError.RatingValidationError.InvalidRating
import mu.KotlinLogging.logger

data class Rating private constructor(
    val rating: Double,
    val votes: Int
) {
    companion object {
        private val log = logger {}

        fun rating(rawRating: String, rawVotes: String): Either<RatingValidationError, Rating> = catch {
            val rating = rawRating.toDouble()
            val votes = rawVotes.replace(",", "").toInt()
            Rating(rating, votes)
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            InvalidRating(rawRating, rawVotes)
        }
    }
}
