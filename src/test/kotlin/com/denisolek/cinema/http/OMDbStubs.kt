package com.denisolek.cinema.http

import com.denisolek.cinema.utils.HttpStub
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class OMDbStubs(port: Int) : HttpStub(port) {
    fun stubFetchMovieSuccess(movieId: String, apiKey: String, response: Any) {
        stubFetchingMovie(movieId, apiKey, OK, response)
    }

    fun stubFetchMovieFailure(movieId: String, apiKey: String, status: HttpStatus) {
        stubFetchingMovie(movieId, apiKey, status, Unit)
    }

    private fun stubFetchingMovie(movieId: String, apiKey: String, status: HttpStatus, response: Any) {
        wiremock.stubFor(
            get(urlEqualTo("/?i=$movieId&apikey=$apiKey"))
                .willReturn(
                    aResponse()
                        .withStatus(status.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(toJson(response))
                )
        )
    }
}