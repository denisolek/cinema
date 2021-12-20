package com.denisolek.cinema.api

import arrow.core.computations.either.eager
import com.denisolek.cinema.domain.authentication.AuthenticationFacade
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.show.AddShow
import com.denisolek.cinema.domain.show.RemoveShow
import com.denisolek.cinema.domain.show.ShowFacade
import com.denisolek.cinema.domain.show.UpdateShow
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(path = ["/shows"], produces = [APPLICATION_JSON_VALUE])
class ShowEndpoint(
    private val authenticationFacade: AuthenticationFacade,
    private val showFacade: ShowFacade
) {

    @GetMapping
    @ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = ShowInfoResponse::class))])
    fun allShows() = showFacade.showInfos().fold(
        ifRight = { ok(ShowInfoResponse(it)) },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PostMapping
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Show added"),
        ApiResponse(responseCode = "404", description = "Movie not found"))
    fun addShow(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody body: AddShowRequest
    ) = eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        showFacade.addShow(AddShow(authentication, MovieId(body.movieId), body.start, body.price)).bind()
    }.fold(
        ifRight = { status(CREATED).build() },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PutMapping("/{showId}")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Ok"),
        ApiResponse(responseCode = "404", description = "Show not found"))
    fun updateShow(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @PathVariable showId: UUID,
        @RequestBody body: UpdateShowRequest
    ) = eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        showFacade.updateShow(UpdateShow(authentication, ShowId(showId), body.start, body.price)).bind()
    }.fold(
        ifRight = { status(OK).build() },
        ifLeft = { it.mapToResponseFailure() }
    )

    @DeleteMapping("/{showId}")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Removed"),
        ApiResponse(responseCode = "404", description = "Show not found"))
    fun removeShow(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @PathVariable showId: UUID
    ) = eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        showFacade.removeShow(RemoveShow(authentication, ShowId(showId))).bind()
    }.fold(
        ifRight = { status(NO_CONTENT).build() },
        ifLeft = { it.mapToResponseFailure() }
    )
}