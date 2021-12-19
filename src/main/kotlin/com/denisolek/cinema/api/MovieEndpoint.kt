package com.denisolek.cinema.api

import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint(private val movieFacade: MovieFacade, private val properties: ApplicationProperties) {
    @GetMapping
    fun movieListing() = movieFacade.getListingInfos().fold(
        ifRight = { ok(MovieListingResponse(it)) },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PostMapping
    fun load() = properties.availableMovies
        .map { MovieId(it) }
        .let { movieFacade.loadMovies(it) }
        .fold(
            ifRight = { ok().build() },
            ifLeft = { it.mapToResponseFailure() }
        )
}