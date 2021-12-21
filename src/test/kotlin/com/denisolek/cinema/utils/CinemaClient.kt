package com.denisolek.cinema.utils

import com.denisolek.cinema.api.AddShowRequest
import com.denisolek.cinema.api.MovieListingResponse
import com.denisolek.cinema.api.ReviewRequest
import com.denisolek.cinema.api.ShowSchedulesResponse
import com.denisolek.cinema.defaults.AuthDefaults.moviegoerToken
import com.denisolek.cinema.defaults.AuthDefaults.ownerToken
import com.denisolek.cinema.domain.readmodel.model.MovieDetails
import com.denisolek.cinema.domain.readmodel.model.MovieShows
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.EMPTY
import org.springframework.http.RequestEntity.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Component
class CinemaClient(val restTemplate: TestRestTemplate) {

    fun movieListing(): ResponseEntity<MovieListingResponse> =
        getRequest("/movies", MovieListingResponse::class.java)

    fun movieDetails(movieId: String): ResponseEntity<MovieDetails> =
        getRequest("/movies/$movieId", MovieDetails::class.java)

    fun movieShows(movieId: String): ResponseEntity<MovieShows> =
        getRequest("/movies/$movieId/shows", MovieShows::class.java)

    fun loadMovies(authorization: String = ownerToken): ResponseEntity<Any> =
        postRequest("/movies", authorizationHeader(authorization), Unit, Any::class.java)

    fun review(movieId: String, stars: Int, authorization: String = moviegoerToken): ResponseEntity<Any> =
        postRequest("/movies/$movieId/reviews", authorizationHeader(authorization), ReviewRequest(stars), Any::class.java)

    fun addShow(movieId: String, start: Instant, price: BigDecimal, authorization: String = ownerToken): ResponseEntity<Any> =
        postRequest("/shows", authorizationHeader(authorization), AddShowRequest(movieId, start, price), Any::class.java)

    fun removeShow(showId: UUID, authorization: String = ownerToken): ResponseEntity<Any> =
        deleteRequest("/shows/$showId", authorizationHeader(authorization), Any::class.java)

    fun showSchedules(): ResponseEntity<ShowSchedulesResponse> =
        getRequest("/shows", ShowSchedulesResponse::class.java)

    fun <T> getRequest(url: String, responseType: Class<T>): ResponseEntity<T> =
        restTemplate.exchange(get(url).build(), responseType)

    fun <T> postRequest(url: String, headers: HttpHeaders = EMPTY, body: Any, responseType: Class<T>): ResponseEntity<T> =
        restTemplate.exchange(post(url).headers(headers).body(body), responseType)

    fun <T> deleteRequest(url: String, headers: HttpHeaders = EMPTY, responseType: Class<T>): ResponseEntity<T> =
        restTemplate.exchange(delete(url).headers(headers).build(), responseType)

    private fun authorizationHeader(authorization: String) = headers("Authorization" to authorization)

    private fun headers(vararg toAdd: Pair<String, String>): HttpHeaders {
        val headers = HttpHeaders()
        toAdd.forEach {
            headers.set(it.first, it.second)
        }
        return headers
    }
}