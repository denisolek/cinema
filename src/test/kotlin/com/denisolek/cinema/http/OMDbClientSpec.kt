package com.denisolek.cinema.http

import com.denisolek.cinema.domain.shared.IOError.*
import com.denisolek.cinema.http.HttpDefaults.defaultOMDbResponse
import com.denisolek.cinema.http.HttpDefaults.omdbApiKey
import com.denisolek.cinema.http.HttpDefaults.omdbBaseUrl
import com.denisolek.cinema.http.HttpDefaults.omdbPort
import com.denisolek.cinema.infrastructure.http.omdb.OMDbClient
import com.denisolek.cinema.infrastructure.http.omdb.OMDbClientProperties
import com.denisolek.cinema.movie.MovieDefaults.defaultMovieData
import com.denisolek.cinema.movie.MovieDefaults.movieId
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.http.HttpStatus.*

class OMDbClientSpec : DescribeSpec({
    val client = OMDbClient(OMDbClientProperties(baseUrl = omdbBaseUrl, basePort = omdbPort, apiKey = omdbApiKey))
    val stub = OMDbStubs(omdbPort)

    it("Should successfully request for data and map it into the domain model") {
        stub.fetchMovieSuccess(movieId.value, omdbApiKey, defaultOMDbResponse)
        val result = client.getMovie(movieId)
        result.shouldBeRight(defaultMovieData)
    }

    // TODO - it should be parametrized data driven test
    describe("Should properly map error codes into domain failures") {
        it("Unauthorized") {
            stub.fetchMovieFailure(movieId.value, omdbApiKey, UNAUTHORIZED)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<Unauthorized>()
        }

        it("Unavailable") {
            stub.fetchMovieFailure(movieId.value, omdbApiKey, SERVICE_UNAVAILABLE)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<Unavailable>()
        }

        it("Client failure") {
            stub.fetchMovieFailure(movieId.value, omdbApiKey, BAD_REQUEST)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<ClientFailure>()
        }

        it("Unknown error") {
            stub.fetchMovieFailure(movieId.value, omdbApiKey, SWITCHING_PROTOCOLS)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<UnknownFailure>()
        }
    }
})