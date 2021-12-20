package com.denisolek.cinema.http

import com.denisolek.cinema.defaults.HttpDefaults.defaultOMDbResponse
import com.denisolek.cinema.defaults.HttpDefaults.omdbApiKey
import com.denisolek.cinema.defaults.HttpDefaults.omdbBaseUrl
import com.denisolek.cinema.defaults.HttpDefaults.omdbPort
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieData
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.domain.shared.IOError.*
import com.denisolek.cinema.infrastructure.http.omdb.OMDbClient
import com.denisolek.cinema.infrastructure.http.omdb.OMDbClientProperties
import com.denisolek.cinema.utils.Stubs.omdb
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.http.HttpStatus.*

class OMDbClientSpec : DescribeSpec({
    val client = OMDbClient(OMDbClientProperties(baseUrl = omdbBaseUrl, basePort = omdbPort, apiKey = omdbApiKey))

    it("Should successfully request for data and map it into the domain model") {
        omdb.stubFetchMovieSuccess(movieId.value, omdbApiKey, defaultOMDbResponse)
        val result = client.getMovie(movieId)
        result.shouldBeRight(defaultMovieData)
    }

    // TODO - it should be parametrized data driven test
    describe("Should properly map error codes into domain failures") {
        it("Unauthorized") {
            omdb.stubFetchMovieFailure(movieId.value, omdbApiKey, UNAUTHORIZED)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<UnauthorizedAccess>()
        }

        it("Unavailable") {
            omdb.stubFetchMovieFailure(movieId.value, omdbApiKey, SERVICE_UNAVAILABLE)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<Unavailable>()
        }

        it("Client failure") {
            omdb.stubFetchMovieFailure(movieId.value, omdbApiKey, BAD_REQUEST)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<ClientFailure>()
        }

        it("Unknown error") {
            omdb.stubFetchMovieFailure(movieId.value, omdbApiKey, SWITCHING_PROTOCOLS)
            val result = client.getMovie(movieId)
            result.shouldBeLeft().shouldBeTypeOf<UnknownFailure>()
        }
    }
})