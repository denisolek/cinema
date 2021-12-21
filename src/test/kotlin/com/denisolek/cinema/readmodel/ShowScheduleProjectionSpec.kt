package com.denisolek.cinema.readmodel

import arrow.core.right
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieListingInfo
import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieLoaded
import com.denisolek.cinema.defaults.MovieDefaults.movieId
import com.denisolek.cinema.defaults.ReadmodelDefaults.defaultShowSchedule
import com.denisolek.cinema.defaults.ShowDefaults.defaultShowAdded
import com.denisolek.cinema.defaults.ShowDefaults.defaultShowPriceUpdated
import com.denisolek.cinema.defaults.ShowDefaults.defaultShowTimeUpdated
import com.denisolek.cinema.defaults.ShowDefaults.showId
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.readmodel.FindShowSchedules
import com.denisolek.cinema.domain.readmodel.ShowScheduleProjection
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.ShowId
import com.denisolek.cinema.domain.show.model.ShowRemoved
import com.denisolek.cinema.infrastructure.persistance.InMemoryShowScheduleRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockk
import java.time.Instant.now
import java.time.temporal.ChronoUnit.DAYS

class ShowScheduleProjectionSpec : FunSpec({
    val repository = InMemoryShowScheduleRepository()
    val movieFacade = mockk<MovieFacade>()
    every { movieFacade.getListingInfo(movieId) } answers { defaultMovieListingInfo.right() }

    val projection = ShowScheduleProjection(repository, movieFacade)

    beforeEach { repository.clear() }

    test("Should create ShowSchedule") {
        projection.handle(defaultShowAdded)

        repository.find(showId).shouldBeRight().shouldBe(defaultShowSchedule)
    }

    test("Should update when movie title changes") {
        projection.handle(defaultShowAdded)
        projection.handle(defaultMovieLoaded.copy(title = "Changed"))

        repository.find(showId).shouldBeRight().movieTitle.shouldBe("Changed")
    }

    test("Should update when show time changes") {
        projection.handle(defaultShowAdded)
        projection.handle(defaultShowTimeUpdated)

        repository.find(showId).shouldBeRight().should {
            it.start.shouldBe(defaultShowTimeUpdated.start)
            it.end.shouldBe(defaultShowTimeUpdated.end)
        }
    }

    test("Should update when show price changes") {
        projection.handle(defaultShowAdded)
        projection.handle(defaultShowPriceUpdated)

        repository.find(showId).shouldBeRight().price.shouldBe(defaultShowPriceUpdated.price)
    }

    test("Should be removed after show removal") {
        projection.handle(defaultShowAdded)
        projection.handle(ShowRemoved(ShowId(showId)))

        repository.find(showId).shouldBeLeft().shouldBeTypeOf<NotFound>()
    }

    test("Should properly query FindShowSchedules") {
        projection.handle(defaultShowAdded)

        projection.query(FindShowSchedules(now().minus(1, DAYS), now().plus(1, DAYS)))
            .shouldBeRight()
            .shouldHaveSize(1)
            .shouldContain(defaultShowSchedule)
    }
})