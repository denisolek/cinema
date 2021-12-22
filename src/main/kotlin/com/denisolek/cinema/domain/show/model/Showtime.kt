package com.denisolek.cinema.domain.show.model


import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.shared.ValidationError.ShowtimeValidationError
import com.denisolek.cinema.domain.shared.ValidationError.ShowtimeValidationError.*
import mu.KotlinLogging.logger
import java.time.Duration
import java.time.Duration.ofMinutes
import java.time.Duration.ofSeconds
import java.time.Instant
import java.time.Instant.now

data class Showtime private constructor(
    val start: Instant,
    val duration: Duration,
) {

    val minutes: Long = duration.toMinutes()
    val end: Instant = start.plus(duration)

    companion object {
        private val log = logger {}
        private val allowedSlippage = ofSeconds(60)

        fun persistedShowtime(start: Instant, duration: Long) = Showtime(start, ofMinutes(duration))

        fun newShowtime(start: Instant, duration: Duration): Either<ShowtimeValidationError, Showtime> =
            showtime(start, duration, true)

        fun existingShowtime(start: Instant, duration: Duration): Either<ShowtimeValidationError, Showtime> =
            showtime(start, duration, false)

        private fun showtime(start: Instant, duration: Duration, newShowtime: Boolean): Either<ShowtimeValidationError, Showtime> = catch {
            if (newShowtime) showtimeCantStartInThePast(start)
            showtimeDurationCantExceed5h(duration)
            Showtime(start, duration)
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            when (ex) {
                is MaxDurationExceededException -> MaxDurationExceeded(duration)
                is ShowtimeInThePastException -> ShowtimeInThePast(start)
                else -> InvalidShowtime(start, duration)
            }
        }

        private fun showtimeDurationCantExceed5h(duration: Duration) {
            if (duration.toMinutes() > 300) throw MaxDurationExceededException()
        }

        private fun showtimeCantStartInThePast(start: Instant) {
            if (start.plusSeconds(allowedSlippage.toSeconds()).isBefore(now())) throw ShowtimeInThePastException()
        }
    }
}

class ShowtimeInThePastException : Throwable()
class MaxDurationExceededException : Throwable()
