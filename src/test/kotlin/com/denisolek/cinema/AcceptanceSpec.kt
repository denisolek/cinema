package com.denisolek.cinema

import com.denisolek.cinema.defaults.HttpDefaults.defaultOMDbResponse
import com.denisolek.cinema.defaults.HttpDefaults.omdbApiKey
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.utils.CinemaClient
import com.denisolek.cinema.utils.Stubs.omdb
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ApplyExtension(SpringTestExtension::class)
class AcceptanceSpec : DescribeSpec() {

    @Autowired
    lateinit var cinema: CinemaClient

    init {
        describe("Cinema") {
            it("Owner loads available movies") {
                omdb.stubFetchMovieSuccess(movieId.value, omdbApiKey, defaultOMDbResponse)
                with(cinema.loadMovies()) {
                    statusCode.shouldBe(OK)
                }
            }

            it("Movies are visible in listing") {
                with(cinema.movieListing()) {
                    statusCode.shouldBe(OK)
                    body?.movies.shouldNotBeEmpty()
                }
            }

            it("Moviegoer should be able to review a move") {
                with(cinema.review(movieId.value, 5)) {
                    statusCode.shouldBe(OK)
                }
            }
        }
    }
}