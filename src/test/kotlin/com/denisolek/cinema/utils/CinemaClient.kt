package com.denisolek.cinema.utils

import com.denisolek.cinema.api.MovieListingResponse
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.RequestEntity.get
import org.springframework.http.RequestEntity.post
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class CinemaClient(val restTemplate: TestRestTemplate) {

    fun movieListing(): ResponseEntity<MovieListingResponse> {
        return get("/movies", MovieListingResponse::class.java)
    }

    fun loadMovies(): ResponseEntity<Any> {
        return post("/movies", Unit, Any::class.java)
    }

    fun <T> get(url: String, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(get(url).build(), responseType)
    }

    fun <T> post(url: String, body: Unit = Unit, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(post(url).body(body), responseType)
    }
}