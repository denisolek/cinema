package com.denisolek.cinema.domain.review.model

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.shared.ValidationError.StarsValidationError
import com.denisolek.cinema.domain.shared.ValidationError.StarsValidationError.InvalidStars
import com.denisolek.cinema.domain.shared.ValidationError.StarsValidationError.StarsOutOfRange
import mu.KotlinLogging.logger

data class Stars private constructor(val value: Int) {

    companion object {
        private val log = logger {}
        private val minStars = 1
        private val maxStars = 5

        fun stars(stars: Int): Either<StarsValidationError, Stars> = catch {
            if (stars < minStars || stars > maxStars) throw StarsOutOfRangeException()
            Stars(stars)
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            when (ex) {
                is StarsOutOfRangeException -> StarsOutOfRange(stars)
                else -> InvalidStars(stars)
            }
        }

        fun persistedStars(stars: Int) = Stars(stars)
    }
}

class StarsOutOfRangeException : Throwable()
