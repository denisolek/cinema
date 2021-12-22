package com.denisolek.cinema.domain.movie.model

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError.InvalidRuntime
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError.TimeUnitNotSupported
import mu.KotlinLogging.logger
import java.time.Duration
import java.time.Duration.ofMinutes

data class Runtime private constructor(val value: Duration) {

    val minutes: Long = value.toMinutes()

    companion object {
        private val log = logger {}

        fun persistedRuntime(duration: Duration) = Runtime(duration)

        fun runtime(value: String): Either<RuntimeValidationError, Runtime> = catch {
            val runtimeParts = value.split(" ")
            if (runtimeParts.size != 2) throw InvalidRuntimeException()
            if (runtimeParts.last() != "min") throw TimeUnitNotSupportedException()
            Runtime(ofMinutes(runtimeParts.first().toLong()))
        }.mapLeft { ex: Throwable ->
            log.error(ex) { ex.message }
            if (ex is TimeUnitNotSupportedException) TimeUnitNotSupported(value)
            else InvalidRuntime(value)
        }
    }
}

class InvalidRuntimeException : Throwable()
class TimeUnitNotSupportedException : Throwable()