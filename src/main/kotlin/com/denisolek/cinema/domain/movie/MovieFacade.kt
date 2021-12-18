package com.denisolek.cinema.domain.movie

import arrow.core.Either
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId

class MovieFacade {
    fun loadMovies(movieIds: List<MovieId>): Either<Failure, Unit> = TODO()
}