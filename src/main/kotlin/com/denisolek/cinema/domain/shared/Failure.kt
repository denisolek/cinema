package com.denisolek.cinema.domain.shared

import com.denisolek.cinema.domain.show.model.Currency
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant

sealed class Failure(val reason: String)

sealed class AuthenticationError(reason: String) : Failure(reason) {
    class Unauthorized : AuthenticationError("Unauthorized")
    class Forbidden : AuthenticationError("Forbidden")
}

sealed class IOError(reason: String) : Failure(reason) {
    class NotFound(resource: String) : IOError("Resource $resource not found")
    class Unavailable(resource: String) : IOError("Resource $resource unavailable")
    class UnauthorizedAccess(resource: String) : IOError("Unauthorized access to $resource")
    class ClientFailure(resource: String) : IOError("Requesting $resource failed with client error")
    class DataIntegrityViolation(resource: String) : IOError("Data integrity violation requesting $resource")
    class UnknownFailure(resource: String) : IOError("Something went wrong requesting $resource")
}

sealed class ValidationError(reason: String) : Failure(reason) {
    sealed class ReleaseDateValidationError(reason: String) : ValidationError(reason) {
        class InvalidReleaseDate(releaseDate: String) : ReleaseDateValidationError("Can't parse $releaseDate")
    }

    sealed class RuntimeValidationError(reason: String) : ValidationError(reason) {
        class TimeUnitNotSupported(unit: String) : RuntimeValidationError("Time unit $unit is not supported")
        class InvalidRuntime(runtime: String) : RuntimeValidationError("Can't parse $runtime")
    }

    sealed class RatingValidationError(reason: String) : ValidationError(reason) {
        class InvalidRating(rating: String, votes: String) : RatingValidationError("Can't parse $rating, $votes")
    }

    sealed class StarsValidationError(reason: String) : ValidationError(reason) {
        class StarsOutOfRange(stars: Int) : StarsValidationError("Stars $stars can't be out of range 0-5")
        class InvalidStars(stars: Int) : StarsValidationError("Can't parse $stars")
    }

    sealed class ShowtimeValidationError(reason: String) : ValidationError(reason) {
        class ShowtimeInThePast(start: Instant) : ShowtimeValidationError("Showtime stars in the past $start")
        class MaxDurationExceeded(duration: Duration) : ShowtimeValidationError("Duration $duration exceeds max show duration")
        class InvalidShowtime(start: Instant, duration: Duration) : ShowtimeValidationError("Can't parse $start, $duration")
    }

    sealed class PriceValidationError(reason: String) : ValidationError(reason) {
        class NegativePrice(amount: BigDecimal) : PriceValidationError("Price $amount can't be negative")
        class InvalidPrice(amount: BigDecimal, currency: Currency) : PriceValidationError("Can't parse $amount, ${currency.name}")
    }
}