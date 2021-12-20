package com.denisolek.cinema.infrastructure.http.omdb

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.denisolek.cinema.domain.movie.MovieData
import com.denisolek.cinema.domain.shared.IOError
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.http.mapToHttpFailure
import com.denisolek.cinema.infrastructure.retry.retry
import io.ktor.client.request.*
import io.ktor.http.*
import mu.KotlinLogging.logger
import org.springframework.stereotype.Component

@Component
class OMDbClient(private val properties: OMDbClientProperties) {
    private val log = logger {}
    private val client = properties.prepareClient()

    suspend fun getMovie(movieId: MovieId): Either<IOError, MovieData> = catch {
        retry {
            client.get<OMDbMovieResponse>("/") {
                parameter("i", movieId.value)
                parameter("apikey", properties.apiKey)
                accept(ContentType.Application.Json)
            }.toMovieData()
        }
    }.mapLeft { ex: Throwable ->
        log.error(ex) { ex.message }
        ex.mapToHttpFailure(movieId.value)
    }
}