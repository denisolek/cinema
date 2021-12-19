package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.movie.model.RatingSource.IMDB
import com.denisolek.cinema.domain.movie.model.RatingSource.INTERNAL
import com.denisolek.cinema.domain.shared.ValidationError.RatingValidationError
import com.denisolek.cinema.domain.shared.ValidationError.RatingValidationError.*
import mu.KotlinLogging.logger
import java.math.BigDecimal
import java.math.BigDecimal.TEN
import java.math.BigDecimal.ZERO

data class Rating private constructor(
    val source: RatingSource,
    val rating: BigDecimal,
    val votes: Long
) {
    companion object {
        private val log = logger {}

        val initialInternalRating = internalRating("0.0", "0")

        fun imdbRating(rating: String, votes: String): Either<RatingValidationError, Rating> = rating(IMDB, rating, votes)

        fun internalRating(rating: String, votes: String): Either<RatingValidationError, Rating> = rating(INTERNAL, rating, votes)

        private fun rating(source: RatingSource, rawRating: String, rawVotes: String): Either<RatingValidationError, Rating> = catch {
            val rating = rawRating.toBigDecimal()
            val votes = rawVotes.replace(",", "").toLong()
            if (rating < ZERO || rating > TEN) throw RatingOutOfRangeException()
            if (votes < 0) throw NegativeVotesException()
            Rating(source, rating, votes)
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            when (ex) {
                is RatingOutOfRangeException -> RatingOutOfRange(rawRating)
                is NegativeVotesException -> NegativeVotes(rawVotes)
                else -> InvalidRating(source, rawRating, rawVotes)
            }
        }
    }
}

enum class RatingSource {
    IMDB, INTERNAL
}

class RatingOutOfRangeException : Throwable()
class NegativeVotesException : Throwable()

