package com.denisolek.cinema.infrastructure.persistance

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.domain.readmodel.infrastructure.MovieDetailsRepository
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.shared.IOError
import org.slf4j.LoggerFactory

class InMemoryMovieDetailsRepository : MovieDetailsRepository {
    private val log = LoggerFactory.getLogger(javaClass)
    private val map: MutableMap<String, MovieDetails> = mutableMapOf()

    override fun save(movie: MovieDetails): Either<IOError, MovieDetails> {
        map[movie.id] = movie
        return movie.right().also { log.info("Saved $it") }
    }

    override fun find(id: String): Either<IOError, MovieDetails> {
        return map[id]?.right() ?: IOError.NotFound(id).left()
    }

    fun clear() = map.clear()
}
