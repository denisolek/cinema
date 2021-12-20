package com.denisolek.cinema.show

import com.denisolek.cinema.domain.shared.ValidationError.ShowtimeValidationError.MaxDurationExceeded
import com.denisolek.cinema.domain.shared.ValidationError.ShowtimeValidationError.ShowtimeInThePast
import com.denisolek.cinema.domain.show.model.Showtime.Companion.existingShowtime
import com.denisolek.cinema.domain.show.model.Showtime.Companion.newShowtime
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Duration.ofMinutes
import java.time.Instant.now
import java.time.Period.ofDays

class ShowtimeSpec : FunSpec({

    test("Should create correct new showtime") {
        val start = now().plus(ofDays(10))
        newShowtime(start, ofMinutes(90))
            .shouldBeRight()
            .should {
                it.start.shouldBe(start)
                it.duration.shouldBe(ofMinutes(90))
            }
    }

    test("Should create correct existing showtime") {
        val start = now().minus(ofDays(10))
        existingShowtime(start, ofMinutes(90))
            .shouldBeRight()
            .should {
                it.start.shouldBe(start)
                it.duration.shouldBe(ofMinutes(90))
            }
    }

    test("Showtime duration can't exceed 5h") {
        val start = now().plus(ofDays(10))
        newShowtime(start, ofMinutes(301))
            .shouldBeLeft()
            .shouldBeTypeOf<MaxDurationExceeded>()
    }

    test("New showtime can't be in the past") {
        val start = now().minusSeconds(61)
        newShowtime(start, ofMinutes(90))
            .shouldBeLeft()
            .shouldBeTypeOf<ShowtimeInThePast>()
    }

    test("New showtime in the past allows 1 minute slippage") {
        val start = now().minusSeconds(60)
        newShowtime(start, ofMinutes(90))
            .shouldBeRight()
            .should {
                it.start.shouldBe(start)
                it.duration.shouldBe(ofMinutes(90))
            }
    }
})