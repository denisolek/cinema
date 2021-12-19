package com.denisolek.cinema.domain.movie.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.movie.model.Movie
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId

interface MovieRepository {
    fun save(movie: Movie): Either<IOError, Movie>
    fun find(movieId: MovieId): Either<IOError, Movie>
}