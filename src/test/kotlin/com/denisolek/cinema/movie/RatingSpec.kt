package com.denisolek.cinema.movie

import com.denisolek.cinema.domain.movie.model.Rating.Companion.rating
import com.denisolek.cinema.domain.shared.ValidationError.RatingValidationError.InvalidRating
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

class RatingSpec : DescribeSpec({
    val correctRating = "6.8"
    val correctVotes = "314"
    val nonNumeric = listOf("a", ",", "0,a", "*43")

    describe("Should create correct") {
        it("rating") {
            rating(correctRating, correctVotes)
                .shouldBeRight()
                .should {
                    it.rating.shouldBe(6.8)
                    it.votes.shouldBe(314L)
                }
        }

        it("rating when votes have commas") {
            rating(correctRating, "817,747,817")
                .shouldBeRight()
                .should {
                    it.rating.shouldBe(6.8)
                    it.votes.shouldBe(817747817L)
                }
        }
    }

    describe("Can't construct with") {
        it("non numeric rating") {
            nonNumeric.forAll {
                rating(it, correctVotes)
                    .shouldBeLeft()
                    .shouldBeTypeOf<InvalidRating>()
            }
        }

        it("non numeric votes") {
            nonNumeric.forAll {
                rating(correctRating, it)
                    .shouldBeLeft()
                    .shouldBeTypeOf<InvalidRating>()
            }
        }
    }
})