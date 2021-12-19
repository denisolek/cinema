package com.denisolek.cinema.movie

import com.denisolek.cinema.domain.movie.model.Runtime.Companion.runtime
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError.InvalidRuntime
import com.denisolek.cinema.domain.shared.ValidationError.RuntimeValidationError.TimeUnitNotSupported
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Duration.ofMinutes

class RuntimeSpec : FunSpec({
    test("Should create correct runtime") {
        runtime("106 min")
            .shouldBeRight()
            .should {
                it.value.shouldBe(ofMinutes(106))
            }
    }

    test("Runtime cannot be in time unit different then minutes") {
        runtime("106 h")
            .shouldBeLeft()
            .shouldBeTypeOf<TimeUnitNotSupported>()
    }

    test("Runtime cannot be in unexpected format") {
        runtime("3 min wrong")
            .shouldBeLeft()
            .shouldBeTypeOf<InvalidRuntime>()
    }

    test("Runtime cannot be constructed from non numeric values") {
        runtime("aaa min")
            .shouldBeLeft()
            .shouldBeTypeOf<InvalidRuntime>()
    }
})