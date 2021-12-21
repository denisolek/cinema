package com.denisolek.cinema

import com.denisolek.cinema.defaults.HttpDefaults.defaultOMDbResponse
import com.denisolek.cinema.defaults.HttpDefaults.omdbApiKey
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieListingInfo
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.defaults.ReadmodelDefaults.defaultMovieDetails
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import com.denisolek.cinema.utils.CinemaClient
import com.denisolek.cinema.utils.Stubs.omdb
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus.OK
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ApplyExtension(SpringTestExtension::class)
class AcceptanceSpec(
    val cinema: CinemaClient,
    val applicationProperties: ApplicationProperties
) : DescribeSpec() {
    init {
        describe("Cinema - success path") {
            val supportedMovies = applicationProperties.supportedMovies.map { MovieId(it) }
            val furiousOne = supportedMovies.first()

            it("Owner loads available movies") {
                supportedMovies.forEach { stubFetchingMovieData(it) }
                with(cinema.loadMovies()) { statusCode.shouldBe(OK) }
            }

            it("Moviegoer check movies in the cinema") {
                with(cinema.movieListing()) {
                    statusCode.shouldBe(OK)
                    body!!.movies
                        .shouldHaveSize(3)
                        .shouldContain(defaultMovieListingInfo)
                }
            }

            it("Moviegoer checks movie details") {
                with(cinema.movieDetails(movieId.value)) {
                    statusCode.shouldBe(OK)
                    body!!.shouldBe(defaultMovieDetails)
                }
            }

            it("Moviegoer reviews movie") {
                with(cinema.review(movieId.value, 5)) {
                    statusCode.shouldBe(OK)
                }
            }

            it("Moviegoer checks if his review changed movie internal rating") {
                with(cinema.movieDetails(movieId.value)) {
                    statusCode.shouldBe(OK)
                    body!!.shouldBe(defaultMovieDetails.copy(internalRating = 5.0, internalVotes = 1))
                }
            }

            it("Owner adds show for a movie")

            it("Moviegoer checks shows for a date")

            it("Moviegoer checks all shows for a movie")
        }
    }

    private fun stubFetchingMovieData(it: MovieId) {
        omdb.stubFetchMovieSuccess(it.value, omdbApiKey, defaultOMDbResponse.copy(imdbID = it.value))
    }
}