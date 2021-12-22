package com.denisolek.cinema.movie

import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.defaults.AuthDefaults.moviegoer
import com.denisolek.cinema.defaults.AuthDefaults.owner
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovie
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieData
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieLoaded
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.movie.infrastructure.MovieDataRepository
import com.denisolek.cinema.domain.movie.model.MovieLoaded
import com.denisolek.cinema.domain.shared.AuthenticationError.Forbidden
import com.denisolek.cinema.domain.shared.IOError.Unavailable
import com.denisolek.cinema.domain.shared.MovieId
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher
import com.denisolek.cinema.infrastructure.persistance.inmemory.InMemoryMovieRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.*

class MovieSpec : DescribeSpec({
    val movieRepository = InMemoryMovieRepository()
    val movieDataRepository = mockk<MovieDataRepository>()
    val eventPublisher = mockk<DomainEventPublisher>()

    val facade = MovieFacade(movieRepository, movieDataRepository, eventPublisher)

    beforeContainer {
        movieRepository.clear()
        clearAllMocks()
    }

    describe("Loading single movie") {
        val movieLoaded = slot<MovieLoaded>()
        every { eventPublisher.publish(capture(movieLoaded)) } answers { }
        every { movieDataRepository.find(listOf(movieId)) } answers { listOf(defaultMovieData).right() }
        facade.loadMovies(owner, listOf(movieId))

        it("should emmit movie loaded event") {
            movieLoaded.captured.shouldBeEqualToIgnoringFields(defaultMovieLoaded, MovieLoaded::id)
        }
        it("should save a movie") {
            movieRepository.find(movieId).shouldBeRight(defaultMovie)
        }
    }

    describe("Loading multiple movies") {
        val movieIds = listOf(MovieId("first"), MovieId("second"), MovieId("third"))
        val expectedMovies = movieIds.map { defaultMovieData.copy(id = it.value) }
        every { movieDataRepository.find(movieIds) } answers { expectedMovies.right() }
        every { eventPublisher.publish(ofType(MovieLoaded::class)) } answers { }
        facade.loadMovies(owner, movieIds)

        it("should process all of them") {
            verify(exactly = 3) { eventPublisher.publish(ofType(MovieLoaded::class)) }
            movieIds.forEach {
                movieRepository.find(it).shouldBeRight()
            }
        }
    }

    describe("Can't load a movie") {
        it("when it fails to fetch movie details") {
            every { movieDataRepository.find(listOf(movieId)) } answers { Unavailable(movieId.value).left() }
            facade.loadMovies(owner, listOf(movieId))
                .shouldBeLeft()
                .shouldBeTypeOf<Unavailable>()
        }
        it("when it's triggered by moviegoer") {
            facade.loadMovies(moviegoer, listOf(movieId))
                .shouldBeLeft()
                .shouldBeTypeOf<Forbidden>()
        }
    }
})