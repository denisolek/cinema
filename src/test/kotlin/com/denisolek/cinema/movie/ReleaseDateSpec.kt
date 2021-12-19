package com.denisolek.cinema.movie

import com.denisolek.cinema.domain.movie.model.ReleaseDate.Companion.releaseDate
import com.denisolek.cinema.domain.shared.ValidationError.ReleaseDateValidationError.InvalidReleaseDate
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Instant.parse

class ReleaseDateSpec : FunSpec({
    test("Should create correct release date") {
        releaseDate("22 Jun 2001")
            .shouldBeRight()
            .should {
                it.value.shouldBe(parse("2001-06-22T00:00:00Z"))
            }
    }

    test("Release date can't be in unsupported format") {
        releaseDate("22-Jun-2001")
            .shouldBeLeft()
            .shouldBeTypeOf<InvalidReleaseDate>()
    }
})