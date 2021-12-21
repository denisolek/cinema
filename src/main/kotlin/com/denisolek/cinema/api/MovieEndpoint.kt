package com.denisolek.cinema.api

import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.authentication.AuthenticationFacade
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.readmodel.FindMovieShows
import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.readmodel.ShowScheduleProjection
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.readmodel.model.MovieShows
import com.denisolek.cinema.domain.review.AddReview
import com.denisolek.cinema.domain.review.ReviewFacade
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit.DAYS

@RestController
@RequestMapping(path = ["/movies"], produces = [APPLICATION_JSON_VALUE])
class MovieEndpoint(
    private val authenticationFacade: AuthenticationFacade,
    private val movieFacade: MovieFacade,
    private val reviewFacade: ReviewFacade,
    private val movieDetailsProjection: MovieDetailsProjection,
    private val showScheduleProjection: ShowScheduleProjection,
    private val properties: ApplicationProperties
) {

    @GetMapping
    @ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = MovieListingResponse::class))])
    fun movieListing() = movieFacade.getListingInfos().fold(
        ifRight = { ok(MovieListingResponse(it)) },
        ifLeft = { it.mapToResponseFailure() }
    )

    @GetMapping("/{movieId}")
    @ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = MovieDetails::class))])
    fun movieDetails(@PathVariable movieId: String) = movieDetailsProjection.query(movieId).fold(
        ifRight = { ok(it) },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PostMapping
    fun load(@RequestHeader(AUTHORIZATION) authorization: String) = eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        properties.supportedMovies
            .map { MovieId(it) }
            .let { movieFacade.loadMovies(authentication, it) }.bind()
    }.fold(
        ifRight = { status(OK).build() },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PostMapping("/{movieId}/reviews")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Review added"),
        ApiResponse(responseCode = "404", description = "Movie not found"))
    fun review(
        @RequestHeader(AUTHORIZATION) authorization: String,
        @PathVariable movieId: String,
        @RequestBody body: ReviewRequest
    ) = eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        reviewFacade.addReview(AddReview(authentication, MovieId(movieId), body.stars)).bind()
    }.fold(
        ifRight = { status(OK).build() },
        ifLeft = { it.mapToResponseFailure() }
    )

    @GetMapping("/{movieId}/shows")
    @ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = MovieShows::class))])
    fun movieShows(
        @PathVariable movieId: String,
        @RequestParam from: Instant?,
        @RequestParam to: Instant?
    ) = eager<Failure, MovieShows> {
        showScheduleProjection.query(FindMovieShows(movieId, from ?: now(), to ?: now().plus(7, DAYS))).bind()
    }.fold(
        ifRight = { ok(it) },
        ifLeft = { it.mapToResponseFailure() }
    )
}