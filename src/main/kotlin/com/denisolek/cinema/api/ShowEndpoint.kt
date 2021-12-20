package com.denisolek.cinema.api

import arrow.core.computations.either
import com.denisolek.cinema.domain.authentication.AuthenticationFacade
import com.denisolek.cinema.domain.shared.Failure
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.show.AddShow
import com.denisolek.cinema.domain.show.ShowFacade
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/shows"], produces = [APPLICATION_JSON_VALUE])
class ShowEndpoint(
    private val authenticationFacade: AuthenticationFacade,
    private val showFacade: ShowFacade
) {
    @GetMapping
    fun allShows() = showFacade.showInfos().fold(
        ifRight = { ok(ShowInfoResponse(it)) },
        ifLeft = { it.mapToResponseFailure() }
    )

    @PostMapping
    fun addShow(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody body: AddShowRequest
    ) = either.eager<Failure, Unit> {
        val authentication = authenticationFacade.authenticate(authorization).bind()
        showFacade.addShow(AddShow(authentication, MovieId(body.movieId), body.start, body.price)).bind()
    }.fold(
        ifRight = { ok().build() },
        ifLeft = { it.mapToResponseFailure() }
    )
}