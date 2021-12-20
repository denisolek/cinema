package com.denisolek.cinema.review

import arrow.core.left
import arrow.core.right
import com.denisolek.cinema.defaults.AuthDefaults.owner
import com.denisolek.cinema.defaults.ReviewDefaults.defaultAddReview
import com.denisolek.cinema.defaults.ReviewDefaults.defaultReview
import com.denisolek.cinema.defaults.ReviewDefaults.defaultReviewAdded
import com.denisolek.cinema.defaults.ReviewDefaults.defaultReviewUpdated
import com.denisolek.cinema.defaults.ReviewDefaults.updatedStars
import com.denisolek.cinema.domain.movie.MovieFacade
import com.denisolek.cinema.domain.review.ReviewFacade
import com.denisolek.cinema.domain.review.model.Review
import com.denisolek.cinema.domain.review.model.ReviewAdded
import com.denisolek.cinema.domain.review.model.ReviewUpdated
import com.denisolek.cinema.domain.shared.AuthenticationError.Forbidden
import com.denisolek.cinema.domain.shared.IOError.NotFound
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.domain.shared.ValidationError.StarsValidationError.StarsOutOfRange
import com.denisolek.cinema.domain.shared.event.DomainEvent
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher
import com.denisolek.cinema.infrastructure.persistance.InMemoryReviewRepository
import com.denisolek.cinema.utils.rightValue
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

class ReviewSpec : DescribeSpec({
    val repository = InMemoryReviewRepository()
    val eventPublisher = mockk<DomainEventPublisher>()
    val movieFacade = mockk<MovieFacade>()

    val facade = ReviewFacade(repository, movieFacade, eventPublisher)

    beforeContainer {
        repository.clear()
        clearAllMocks()
        every { movieFacade.movieExists(any()) } answers { true.right() }
    }

    describe("Reviewing a movie") {
        val reviewAdded = slot<List<ReviewAdded>>()
        every { eventPublisher.publish(capture(reviewAdded)) } answers { }
        facade.addReview(defaultAddReview)

        it("should emmit review added event") {
            reviewAdded.captured
                .first()
                .shouldBeEqualToIgnoringFields(defaultReviewAdded, ReviewAdded::id, ReviewAdded::reviewId, ReviewAdded::date)
        }

        it("should save a review") {
            val reviewId = ReviewId(reviewAdded.captured.first().reviewId)
            repository.find(reviewId)
                .shouldBeRight()
                .shouldBeEqualToIgnoringFields(defaultReview.copy(id = reviewId), Review::date)
        }
    }

    describe("Can't review a movie") {
        it("when it doesn't exist") {
            every { movieFacade.movieExists(any()) } answers { NotFound("").left() }
            facade.addReview(defaultAddReview)
                .shouldBeLeft()
                .shouldBeTypeOf<NotFound>()
        }
        every { movieFacade.movieExists(any()) } answers { true.right() }
        it("if given stars are out of range") {
            facade.addReview(defaultAddReview.copy(stars = 6))
                .shouldBeLeft()
                .shouldBeTypeOf<StarsOutOfRange>()
        }
        it("when it's triggered by owner") {
            facade.addReview(defaultAddReview.copy(authentication = owner))
                .shouldBeLeft()
                .shouldBeTypeOf<Forbidden>()
        }
    }

    describe("Reviewing already reviewed movie") {
        val reviewAdded = slot<List<DomainEvent>>()
        every { eventPublisher.publish(capture(reviewAdded)) } answers { }
        facade.addReview(defaultAddReview)
        val capturedReviewAdded = reviewAdded.captured.first() as ReviewAdded
        val existingReviewId = ReviewId(capturedReviewAdded.reviewId)

        it("should update existing review") {
            facade.addReview(defaultAddReview.copy(stars = updatedStars.rightValue.value))
            repository.find(existingReviewId)
                .shouldBeRight().should {
                    it.stars.shouldBe(updatedStars.rightValue)
                    it.date.shouldNotBe(capturedReviewAdded.date)
                }
        }

        it("should emmit review updated event") {
            reviewAdded.captured
                .first()
                .shouldBeEqualToUsingFields(defaultReviewUpdated, ReviewUpdated::stars)
        }
    }
})