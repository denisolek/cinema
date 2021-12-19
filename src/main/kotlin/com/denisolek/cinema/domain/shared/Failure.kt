package com.denisolek.cinema.domain.shared

import com.denisolek.cinema.domain.movie.model.RatingSource

sealed class Failure(val reason: String)

sealed class IOError(reason: String) : Failure(reason) {
    class NotFound(resource: String) : IOError("Resource $resource not found")
    class Unavailable(resource: String) : IOError("Resource $resource unavailable")
    class Unauthorized(resource: String) : IOError("Unauthorized access to $resource")
    class ClientFailure(resource: String) : IOError("Requesting $resource failed with client error")
    class UnknownFailure(resource: String) : IOError("Something went wrong requesting $resource")
}

sealed class ValidationError(reason: String) : Failure(reason) {
    sealed class ReleaseDateValidationError(reason: String) : ValidationError(reason) {
        class InvalidReleaseDate(releaseDate: String) : ReleaseDateValidationError("Can't parse $releaseDate")
    }

    sealed class RuntimeValidationError(reason: String) : Failure(reason) {
        class TimeUnitNotSupported(unit: String) : RuntimeValidationError("Time unit $unit is not supported")
        class InvalidRuntime(runtime: String) : RuntimeValidationError("Can't parse $runtime")
    }

    sealed class RatingValidationError(reason: String) : Failure(reason) {
        class RatingOutOfRange(rating: String) : RatingValidationError("Rating $rating can't be out of range 0-10")
        class NegativeVotes(votes: String) : RatingValidationError("Votes $votes can't be negative")
        class InvalidRating(source: RatingSource, rating: String, votes: String) : RatingValidationError("Can't parse $source, $rating, $votes")
    }
}