package com.denisolek.cinema

import com.denisolek.cinema.defaults.HttpDefaults.defaultOMDbResponse
import com.denisolek.cinema.defaults.HttpDefaults.omdbApiKey
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieListingInfo
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.defaults.MovieDefaults.movieTitle
import com.denisolek.cinema.defaults.ReadmodelDefaults.defaultMovieDetails
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.infrastructure.ApplicationProperties
import com.denisolek.cinema.utils.CinemaClient
import com.denisolek.cinema.utils.Stubs.omdb
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpStatus.*
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal.valueOf
import java.time.Instant.now
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.SECONDS

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

            val newShowStart = now().plus(3, DAYS)

            it("Owner adds show for a movie") {
                with(cinema.addShow(movieId.value, newShowStart, valueOf(9))) {
                    statusCode.shouldBe(CREATED)
                }
            }

            it("Owner check shows for a week") {
                with(cinema.showSchedules()) {
                    statusCode.shouldBe(OK)
                    body!!.schedules
                        .shouldHaveSize(1)
                        .first().should {
                            it.movieId.shouldBe(movieId.value)
                            it.start.truncatedTo(SECONDS).shouldBe(newShowStart.truncatedTo(SECONDS))
                            it.price.shouldBe(9)
                        }
                }
            }

            it("Moviegoer checks movie shows for a week") {
                with(cinema.movieShows(movieId.value)) {
                    statusCode.shouldBe(OK)
                    body!!.should {
                        it.movieId.shouldBe(movieId.value)
                        it.title.shouldBe(movieTitle)
                        it.shows.first().should { show ->
                            show.start.truncatedTo(SECONDS).shouldBe(newShowStart.truncatedTo(SECONDS))
                            show.price.shouldBe(9)
                        }
                    }
                }
            }

            it("Owner removes show") {
                val showId = cinema.showSchedules().body!!.schedules.first().showId
                cinema.removeShow(showId)
                    .statusCode.shouldBe(NO_CONTENT)
                with(cinema.showSchedules()) {
                    statusCode.shouldBe(OK)
                    body!!.schedules.shouldBeEmpty()
                }
            }
        }
    }

    private fun stubFetchingMovieData(it: MovieId) {
        omdb.stubFetchMovieSuccess(it.value, omdbApiKey, defaultOMDbResponse.copy(imdbID = it.value))
    }
}