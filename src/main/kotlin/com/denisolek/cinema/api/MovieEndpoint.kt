package com.denisolek.cinema.api

import com.denisolek.cinema.domain.movie.MovieFacade
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint(private val movieFacade: MovieFacade) {
    @GetMapping
    fun moviesListing() = movieFacade.getListingInfos().fold(
        ifRight = { ok(MovieListingResponse(it)) },
        ifLeft = { it.mapToResponseFailure() }
    )
}