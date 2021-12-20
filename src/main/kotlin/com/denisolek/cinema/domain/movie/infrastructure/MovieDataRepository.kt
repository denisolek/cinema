package com.denisolek.cinema.domain.movie.infrastructure

import arrow.core.Either
import com.denisolek.cinema.domain.movie.MovieData
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId

interface MovieDataRepository {
    fun find(movieIds: List<MovieId>): Either<IOError, List<MovieData>>
}

