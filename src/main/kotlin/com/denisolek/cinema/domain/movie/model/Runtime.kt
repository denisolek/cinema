package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.left
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError.InvalidRuntime
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError.TimeUnitNotSupported
import mu.KotlinLogging.logger
import java.time.Duration
import java.time.Duration.ofMinutes

data class Runtime private constructor(val value: Duration) {
    companion object {
        private val log = logger {}

        fun runtime(value: String): Either<RuntimeValidationError, Runtime> =
            value.split(" ").let {
                when {
                    it.size != 2 -> InvalidRuntime(value).left()
                    it.last() != "min" -> TimeUnitNotSupported(it.last()).left()
                    else -> catch { Runtime(ofMinutes(it.first().toLong())) }.mapLeft { ex: Throwable ->
                        log.error(ex) { ex.message }
                        InvalidRuntime(value)
                    }
                }
            }
    }
}