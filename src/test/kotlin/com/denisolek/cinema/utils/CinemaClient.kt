package com.denisolek.cinema.utils

import com.denisolek.cinema.api.MovieListingResponse
import com.denisolek.cinema.defaults.AuthDefaults.ownerToken
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.EMPTY
import org.springframework.http.RequestEntity.get
import org.springframework.http.RequestEntity.post
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class CinemaClient(val restTemplate: TestRestTemplate) {

    fun movieListing(): ResponseEntity<MovieListingResponse> {
        return get("/movies", MovieListingResponse::class.java)
    }

    fun loadMovies(authorization: String = ownerToken): ResponseEntity<Any> {
        return post("/movies", authorizationHeader(authorization), Unit, Any::class.java)
    }

    fun <T> get(url: String, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(get(url).build(), responseType)
    }

    fun <T> post(url: String, headers: HttpHeaders = EMPTY, body: Unit, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(
            post(url)
                .headers(headers)
                .body(body), responseType
        )
    }

    private fun authorizationHeader(authorization: String) = headers("Authorization" to authorization)

    private fun headers(vararg toAdd: Pair<String, String>): HttpHeaders {
        val headers = HttpHeaders()
        toAdd.forEach {
            headers.set(it.first, it.second)
        }
        return headers
    }
}