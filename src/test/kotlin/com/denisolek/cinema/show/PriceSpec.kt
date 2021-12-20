package com.denisolek.cinema.show

import com.denisolek.cinema.domain.shared.ValidationError.PriceValidationError.NegativePrice
import com.denisolek.cinema.domain.show.model.Currency.EUR
import com.denisolek.cinema.domain.show.model.Currency.USD
import com.denisolek.cinema.domain.show.model.Price.Companion.price
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.math.BigDecimal.valueOf

class PriceSpec : FunSpec({

    test("Should create correct price") {
        val amount = valueOf(13.71)
        price(amount)
            .shouldBeRight()
            .should {
                it.amount.shouldBe(amount)
                it.currency.shouldBe(USD)
            }
    }

    test("Should support other currency") {
        val amount = valueOf(44.24)
        price(amount, EUR)
            .shouldBeRight()
            .should {
                it.amount.shouldBe(amount)
                it.currency.shouldBe(EUR)
            }
    }

    test("Price can't be negative") {
        price(valueOf(-1))
            .shouldBeLeft()
            .shouldBeTypeOf<NegativePrice>()
    }

    test("Price should be scaled to 2") {
        price(valueOf(176.73612516))
            .shouldBeRight()
            .amount.shouldBe(valueOf(176.74))
    }
})