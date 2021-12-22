package com.denisolek.cinema.review

import com.denisolek.cinema.defaults.ReviewDefaults.defaultReview
import com.denisolek.cinema.defaults.ReviewDefaults.defaultReviewAdded
import com.denisolek.cinema.domain.review.ReviewEventHandler
import com.denisolek.cinema.domain.review.model.Stars.Companion.stars
import com.denisolek.cinema.domain.review.model.SummedReviewChanged
import com.denisolek.cinema.domain.shared.ReviewId
import com.denisolek.cinema.domain.shared.UserId
import com.denisolek.cinema.domain.shared.event.DomainEventPublisher
import com.denisolek.cinema.infrastructure.persistence.inmemory.InMemoryReviewRepository
import com.denisolek.cinema.utils.rightValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

class ReviewEventHandlerSpec : FunSpec({
    val repository = InMemoryReviewRepository()
    val eventPublisher = mockk<DomainEventPublisher>()

    val handler = ReviewEventHandler(repository, eventPublisher)

    test("Should handle ReviewAdded emitting SummedReviewChanged") {
        listOf(1, 5, 4, 2, 5).map { saveReview(repository, it) }
        val summedReviewChanged = slot<SummedReviewChanged>()
        every { eventPublisher.publish(capture(summedReviewChanged)) } answers { }
        handler.handle(defaultReviewAdded)
        summedReviewChanged.captured.should {
            it.stars.shouldBe(3.4)
            it.votes.shouldBe(5)
        }
    }
})

private fun saveReview(repository: InMemoryReviewRepository, stars: Int) {
    repository.save(
        defaultReview.copy(
            id = ReviewId(),
            userId = UserId("${(10000..99999).random()}"),
            stars = stars(stars).rightValue
        )
    )
}