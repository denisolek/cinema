package com.denisolek.cinema.domain.readmodel.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.shared.IOError

interface MovieDetailsRepository {
    fun save(movie: MovieDetails): Either<IOError, Unit>
    fun find(id: String): Either<IOError, MovieDetails>
}