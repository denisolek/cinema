package com.denisolek.cinema.review

import com.denisolek.cinema.domain.review.model.Stars.Companion.stars
import com.denisolek.cinema.domain.shared.ValidationError.StarsValidationError.StarsOutOfRange
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.types.shouldBeTypeOf

class StarsSpec : FunSpec({
    test("Should create correct stars") {
        listOf(1, 2, 3, 4, 5).forAll {
            stars(it).shouldBeRight()
        }
    }

    test("Stars can't be out of range 1-5") {
        listOf(-1, 6).forAll {
            stars(it)
                .shouldBeLeft()
                .shouldBeTypeOf<StarsOutOfRange>()
        }
    }
})