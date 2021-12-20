package com.denisolek.cinema.movie

import com.denisolek.cinema.domain.movie.model.Rating.Companion.imdbRating
import com.denisolek.cinema.domain.movie.model.Rating.Companion.internalRating
import com.denisolek.cinema.domain.movie.model.RatingSource.IMDB
import com.denisolek.cinema.domain.movie.model.RatingSource.INTERNAL
import com.denisolek.cinema.domain.shared.ValidationError.RatingValidationError.*
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.math.BigDecimal.valueOf

class RatingSpec : DescribeSpec({
    val correctRating = "6.8"
    val correctVotes = "314"
    val nonNumeric = listOf("a", ",", "0,a", "*43")

    describe("Should create correct") {
        it("imdb rating") {
            imdbRating(correctRating, correctVotes)
                .shouldBeRight()
                .should {
                    it.source.shouldBe(IMDB)
                    it.rating.shouldBe(valueOf(6.8))
                    it.votes.shouldBe(314L)
                }
        }

        it("internal rating") {
            internalRating(correctRating, correctVotes)
                .shouldBeRight()
                .should {
                    it.source.shouldBe(INTERNAL)
                    it.rating.shouldBe(valueOf(6.8))
                    it.votes.shouldBe(314L)
                }
        }
        it("rating when votes have commas") {
            internalRating(correctRating, "817,747,817")
                .shouldBeRight()
                .should {
                    it.source.shouldBe(INTERNAL)
                    it.rating.shouldBe(valueOf(6.8))
                    it.votes.shouldBe(817747817L)
                }
        }
    }

    describe("Can't construct with") {
        it("non numeric rating") {
            nonNumeric.forAll {
                internalRating(it, correctVotes).shouldBeLeft().shouldBeTypeOf<InvalidRating>()
            }
        }

        it("rating out of range") {
            internalRating("-1", correctVotes).shouldBeLeft().shouldBeTypeOf<RatingOutOfRange>()
            internalRating("11", correctVotes).shouldBeLeft().shouldBeTypeOf<RatingOutOfRange>()
        }

        it("non numeric votes") {
            nonNumeric.forAll {
                internalRating(correctRating, it).shouldBeLeft().shouldBeTypeOf<InvalidRating>()
            }
        }

        it("negative votes") {
            internalRating(correctRating, "-1").shouldBeLeft().shouldBeTypeOf<NegativeVotes>()
        }
    }
})