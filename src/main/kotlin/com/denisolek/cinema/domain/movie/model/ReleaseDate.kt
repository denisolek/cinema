package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.shared.ValidationError.ReleaseDateValidationError
import com.denisolek.cinema.domain.shared.ValidationError.ReleaseDateValidationError.InvalidReleaseDate
import mu.KotlinLogging.logger
import java.time.Instant
import java.time.LocalDate.parse
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale.US

data class ReleaseDate private constructor(val value: Instant) {
    companion object {
        private val log = logger {}
        private val formatter = ofPattern("dd MMM yyyy", US)

        fun releaseDate(value: String): Either<ReleaseDateValidationError, ReleaseDate> = catch {
            ReleaseDate(parse(value, formatter).atStartOfDay().toInstant(UTC))
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            InvalidReleaseDate(value)
        }
    }
}