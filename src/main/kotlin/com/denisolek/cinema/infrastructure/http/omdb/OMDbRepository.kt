package com.denisolek.cinema.infrastructure.http.omdb

import arrow.core.Either
import arrow.core.sequenceEither
import arrow.fx.coroutines.parTraverseN
import com.denisolek.cinema.domain.movie.infrastructure.MovieData
import com.denisolek.cinema.domain.movie.infrastructure.MovieDataRepository
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class OMDbRepository(private val client: OMDbClient, private val properties: OMDbClientProperties) : MovieDataRepository {
    override fun find(movieIds: List<MovieId>): Either<IOError, List<MovieData>> = runBlocking {
        movieIds.parTraverseN(properties.concurrentOperationsLimit) {
            client.getMovie(it)
        }.sequenceEither()
    }
}