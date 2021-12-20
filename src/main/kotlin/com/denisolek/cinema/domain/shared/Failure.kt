package com.denisolek.cinema.domain.shared

import com.denisolek.cinema.domain.movie.model.RatingSource

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
        class RatingOutOfRange(rating: String) : RatingValidationError("Rating $rating can't be out of range (imdb 0-10, internal 0-5)")
        class NegativeVotes(votes: String) : RatingValidationError("Votes $votes can't be negative")
        class InvalidRating(source: RatingSource, rating: String, votes: String) : RatingValidationError("Can't parse $source, $rating, $votes")
    }

    sealed class StarsValidationError(reason: String) : ValidationError(reason) {
        class StarsOutOfRange(stars: Int) : StarsValidationError("Stars $stars can't be out of range 0-5")
        class InvalidStars(stars: Int) : StarsValidationError("Can't parse $stars")
    }
}