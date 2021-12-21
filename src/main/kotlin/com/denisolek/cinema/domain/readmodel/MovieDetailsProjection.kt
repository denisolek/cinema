package com.denisolek.cinema.domain.readmodel

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import com.denisolek.cinema.domain.shared.IOError

class MovieDetailsProjection(val repository: MovieDetailsRepository) {
    fun handle(event: MovieLoaded) {
        repository.find(event.movieId).fold(
            ifLeft = {
                if (it is IOError.NotFound) MovieDetails(event).right()
                else it.left()
            },
            ifRight = { it.apply(event).right() }
        ).flatMap {
            repository.save(it)
        }
    }

    fun handle(event: SummedReviewChanged) {
        repository.find(event.movieId).map {
            repository.save(it.apply(event))
        }
    }

    fun query(movieId: String): Either<IOError, MovieDetails> = repository.find(movieId)
}