package com.denisolek.cinema.infrastructure.persistence.inmemory

import arrow.core.Either
import arrow.core.computations.either.eager
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.movie.infrastructure.MovieRepository
import com.denisolek.cinema.domain.movie.model.Movie
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.MovieId
import mu.KotlinLogging.logger

class InMemoryMovieRepository : MovieRepository {
    private val log = logger {}
    private val movies: MutableMap<MovieId, Movie> = mutableMapOf()

    override fun save(movie: Movie): Either<IOError, Unit> = eager {
        log.info { "Saving $movie" }
        movies[movie.id] = movie
    }

    override fun find(movieId: MovieId): Either<IOError, Movie> =
        movies[movieId]?.right() ?: NotFound(movieId.value).left()

    override fun findAll(): Either<IOError, List<Movie>> =
        movies.values.toList().right()

    fun clear() = movies.clear()
}
