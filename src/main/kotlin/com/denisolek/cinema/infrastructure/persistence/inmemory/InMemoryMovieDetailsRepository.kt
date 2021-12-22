package com.denisolek.cinema.infrastructure.persistence.inmemory

import arrow.core.Either
import arrow.core.computations.either.eager
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.shared.IOError
import org.slf4j.LoggerFactory

class InMemoryMovieDetailsRepository : MovieDetailsRepository {
    private val log = LoggerFactory.getLogger(javaClass)
    private val movieDetails: MutableMap<String, MovieDetails> = mutableMapOf()

    override fun save(movie: MovieDetails): Either<IOError, Unit> = eager {
        log.info("Saving $movie")
        movieDetails[movie.id] = movie
    }

    override fun find(id: String): Either<IOError, MovieDetails> =
        movieDetails[id]?.right() ?: IOError.NotFound(id).left()

    fun clear() = movieDetails.clear()
}
