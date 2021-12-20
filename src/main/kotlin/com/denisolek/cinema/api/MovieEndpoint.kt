package com.denisolek.cinema.api

import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.authentication.AuthenticationFacade
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint(
    private val authenticationFacade: AuthenticationFacade,
    private val movieFacade: MovieFacade,
    private val properties: ApplicationProperties
) {
    @GetMapping
    fun movieListing() = movieFacade.getListingInfos().fold(
        ifRight = { ok(MovieListingResponse(it)) },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PostMapping
    fun load(@RequestHeader(AUTHORIZATION) authorization: String): ResponseEntity<Any> = eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        properties.availableMovies
            .map { MovieId(it) }
            .let { movieFacade.loadMovies(authentication, it) }.bind()
    }.fold(
        ifRight = { ok().build() },
        ifLeft = { it.mapToResponseFailure() }
    )
}