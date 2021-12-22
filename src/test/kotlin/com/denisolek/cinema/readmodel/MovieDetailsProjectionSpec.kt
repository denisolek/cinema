package com.denisolek.cinema.readmodel

import com.denisolek.cinema.defaults.MovieDefaults.defaultMovieLoaded
import com.denisolek.cinema.defaults.ReadmodelDefaults.defaultMovieDetails
import com.denisolek.cinema.domain.readmodel.MovieDetailsProjection
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import com.denisolek.cinema.infrastructure.persistance.inmemory.InMemoryMovieDetailsRepository
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.Instant.now

class MovieDetailsProjectionSpec : FunSpec({
    val repository = InMemoryMovieDetailsRepository()
    val projection = MovieDetailsProjection(repository)

    beforeEach { repository.clear() }

    test("Should create MovieDetails") {
        projection.handle(defaultMovieLoaded)

        repository.find(defaultMovieLoaded.movieId).shouldBeRight().shouldBe(defaultMovieDetails)
    }

    test("Should update existing MovieDetails") {
        projection.handle(defaultMovieLoaded)
        projection.handle(defaultMovieLoaded.copy(title = "Changed"))

        repository.find(defaultMovieLoaded.movieId).shouldBeRight().title.shouldBe("Changed")
    }

    test("Should apply last internal rating change") {
        projection.handle(defaultMovieLoaded)
        projection.handle(summedReviewChanged(stars = 5.0, votes = 1))
        projection.handle(summedReviewChanged(stars = 4.5, votes = 2))
        projection.handle(summedReviewChanged(stars = 3.67, votes = 3))

        repository.find(defaultMovieLoaded.movieId).shouldBeRight().should {
            it.internalRating.shouldBe(3.67)
            it.internalVotes.shouldBe(3)
        }
    }
})

private fun summedReviewChanged(stars: Double, votes: Int) = SummedReviewChanged(
    movieId = defaultMovieLoaded.movieId,
    date = now(),
    stars = stars,
    votes = votes
)